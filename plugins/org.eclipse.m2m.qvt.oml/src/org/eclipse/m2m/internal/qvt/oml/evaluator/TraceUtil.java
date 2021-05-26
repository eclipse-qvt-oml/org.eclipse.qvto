/*******************************************************************************
 * Copyright (c) 2008, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *   
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *     Alex Paperno - bugs 424584
 *     Yuri Blankenstein - bug 428325
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.evaluator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.AbstractEList;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.m2m.internal.qvt.oml.ast.env.InternalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.QvtOperationalEvaluationEnv;
import org.eclipse.m2m.internal.qvt.oml.ast.env.TupleFactory;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalParserUtil;
import org.eclipse.m2m.internal.qvt.oml.ast.parser.QvtOperationalUtil;
import org.eclipse.m2m.internal.qvt.oml.expressions.DirectionKind;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingOperation;
import org.eclipse.m2m.internal.qvt.oml.expressions.MappingParameter;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModelType;
import org.eclipse.m2m.internal.qvt.oml.expressions.Module;
import org.eclipse.m2m.internal.qvt.oml.expressions.ModuleImport;
import org.eclipse.m2m.internal.qvt.oml.expressions.VarParameter;
import org.eclipse.m2m.internal.qvt.oml.trace.EDirectionKind;
import org.eclipse.m2m.internal.qvt.oml.trace.EMappingContext;
import org.eclipse.m2m.internal.qvt.oml.trace.EMappingOperation;
import org.eclipse.m2m.internal.qvt.oml.trace.EMappingParameters;
import org.eclipse.m2m.internal.qvt.oml.trace.EMappingResults;
import org.eclipse.m2m.internal.qvt.oml.trace.ETuplePartValue;
import org.eclipse.m2m.internal.qvt.oml.trace.EValue;
import org.eclipse.m2m.internal.qvt.oml.trace.Trace;
import org.eclipse.m2m.internal.qvt.oml.trace.TraceFactory;
import org.eclipse.m2m.internal.qvt.oml.trace.TraceRecord;
import org.eclipse.m2m.internal.qvt.oml.trace.VarParameterValue;
import org.eclipse.m2m.qvt.oml.util.Dictionary;
import org.eclipse.m2m.qvt.oml.util.MutableList;
import org.eclipse.m2m.qvt.oml.util.Utils;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.types.BagType;
import org.eclipse.ocl.types.CollectionType;
import org.eclipse.ocl.types.OrderedSetType;
import org.eclipse.ocl.types.PrimitiveType;
import org.eclipse.ocl.types.SequenceType;
import org.eclipse.ocl.types.SetType;
import org.eclipse.ocl.types.TupleType;
import org.eclipse.ocl.util.Bag;
import org.eclipse.ocl.util.CollectionUtil;
import org.eclipse.ocl.util.Tuple;
import org.eclipse.ocl.util.TypeUtil;
import org.eclipse.ocl.utilities.PredefinedType;

public class TraceUtil {

	private TraceUtil() {
	}

	static TraceRecord getTraceRecord(QvtOperationalEvaluationEnv evalEnv, MappingOperation mappingOperation) {
		InternalEvaluationEnv internEnv = evalEnv.getAdapter(InternalEvaluationEnv.class);
		Trace trace = internEnv.getTraces();

		Object selfObj = evalEnv.getValueOf(Environment.SELF_VARIABLE_NAME);

		Object key = createKey(selfObj, evalEnv, mappingOperation);
		if (key != null) {
			TraceRecord record = trace.getRecordBySource(mappingOperation, key);
			if (record != null && Boolean.TRUE.equals(checkResultMatch(record, evalEnv))) {
				return record;
			}
			// nothing found, mapping executed for the first time on the given
			// source
			return null;
		}

		// Fall back on 'original' TraceUtil
		return getTraceRecordDefault(evalEnv, mappingOperation);
	}

    private static TraceRecord getTraceRecordDefault(QvtOperationalEvaluationEnv evalEnv, MappingOperation mappingOperation) {
    	InternalEvaluationEnv internEnv = evalEnv.getAdapter(InternalEvaluationEnv.class);    	
    	Trace trace = internEnv.getTraces();
        Object selfObj = evalEnv.getValueOf(Environment.SELF_VARIABLE_NAME);
    	
    	// the direct fetch by the contextual source object
    	if(selfObj != null && isParameterLessContextual(mappingOperation)) {
	    	TraceRecord record = trace.getRecordBySource(mappingOperation, selfObj);
	    	if(record != null && Boolean.TRUE.equals(checkResultMatch(record, evalEnv))) {
	    		return record;
	    	}
	    	// nothing found, mapping executed for the first time on the given source
	    	return null;
    	}

        EMap<MappingOperation, EList<TraceRecord>> allTraceRecordMap = trace.getTraceRecordMap();
        EList<TraceRecord> traceRecords = allTraceRecordMap.get(mappingOperation);
        if (traceRecords == null) {
            return null;
        }
        
        // Section [8.2.1.15]
        // After call resolution, all the parameters of the mapping are passed as a tuple. 
        // The parameters include, in this order: the context parameter (if any), the owned 
        // parameters (from Operation::ownedParameter), and the parameters declared as result.

        traceCheckCycle:
            for (TraceRecord nextRecord : traceRecords) {
            	// check context parameter
                if (QvtOperationalParserUtil.isContextual(mappingOperation)) {
                    VarParameterValue nextContext = nextRecord.getContext().getContext();
					if (nextContext == null) {
                        continue;
                    }
                    if (!isOclEqual(selfObj, nextContext.getValue().getOclObject(), mappingOperation.getContext().getKind(), evalEnv)) {
                        continue;
                    }
                }
                // check owned parameters
                int candidateParamSize = mappingOperation.getEParameters().size();
                if (nextRecord.getParameters().getParameters().size() != candidateParamSize) {
                    continue;
                }
                for (int i = 0; i < candidateParamSize; i++) {
                    EParameter param = mappingOperation.getEParameters().get(i);
                    Object paramValue = evalEnv.getValueOf(param.getName());
                    VarParameterValue traceParamVal = (VarParameterValue) nextRecord.getParameters().getParameters().get(i);
                    DirectionKind paramKind = DirectionKind.IN;
                    if (param instanceof VarParameter) {
                    	paramKind = ((VarParameter) param).getKind();
                    }
                    if (paramKind != DirectionKind.OUT
                    		&& !isOclEqual(paramValue, traceParamVal.getValue().getOclObject(), paramKind, evalEnv)) {
                        continue traceCheckCycle;
                    }
                }
                
                // check result parameters                
                Boolean checkResult = checkResultMatch(nextRecord, evalEnv);
                if(checkResult == null) {
                	continue;
                } else if(Boolean.FALSE.equals(checkResult)) {
                	continue traceCheckCycle;
                }
                
                return nextRecord;
            }
        return null;
    }
    
    static TraceRecord addTraceRecord(QvtOperationalEvaluationEnv evalEnv, MappingOperation mappingOperation) {
        TraceRecord traceRecord = TraceFactory.eINSTANCE.createTraceRecord();
        
        InternalEvaluationEnv internEnv = evalEnv.getAdapter(InternalEvaluationEnv.class);
        Trace trace = internEnv.getTraces();
        EList<TraceRecord> allRecList = createOrGetListElementFromMap(trace.getTraceRecordMap(), mappingOperation);        
        addUnique(traceRecord, allRecList);

        EMappingOperation eMappingOperation = TraceFactory.eINSTANCE.createEMappingOperation();
        traceRecord.setMappingOperation(eMappingOperation);
        eMappingOperation.setName(mappingOperation.getName());
        Module module = QvtOperationalParserUtil.getOwningModule(mappingOperation);
        eMappingOperation.setPackage(module.getNsPrefix());
        eMappingOperation.setModule(module.getName());
        eMappingOperation.setRuntimeMappingOperation(mappingOperation);

        EMappingContext eMappingContext = TraceFactory.eINSTANCE.createEMappingContext();
        traceRecord.setContext(eMappingContext);
        if(QvtOperationalParserUtil.isContextual(mappingOperation)) {
            VarParameter operContext = mappingOperation.getContext();
            VarParameterValue contextVPV = createVarParameterValue(mappingOperation,
                    operContext.getKind(), operContext.getEType(), Environment.SELF_VARIABLE_NAME, evalEnv);
            eMappingContext.setContext(contextVPV);
            EList<TraceRecord> contextMappings = createOrGetListElementFromMap(trace.getSourceToTraceRecordMap(), contextVPV.getValue().getOclObject());
            addUnique(traceRecord, contextMappings);
        }
        else {
        	// make the first in parameter as the mapping source object
        	for (EParameter nextEParam : mappingOperation.getEParameters()) {
        		if(nextEParam instanceof VarParameter) {
        			VarParameter firstInVarParam = (VarParameter) nextEParam;
        			if((firstInVarParam.getEType() instanceof PredefinedType<?> == false) 
        					&& (firstInVarParam.getKind() == DirectionKind.IN || firstInVarParam.getKind() == DirectionKind.INOUT)) {
        				Object val = createVarParameterValue(mappingOperation, firstInVarParam.getKind() ,
        							firstInVarParam.getEType(), firstInVarParam.getName(), evalEnv).getValue().getOclObject();        	
        				EList<TraceRecord> sourceMappings = createOrGetListElementFromMap(trace.getSourceToTraceRecordMap(), val);
        				addUnique(traceRecord, sourceMappings);
        				break;
        			}
        		}
			}
        }
        
        EMappingParameters eMappingParameters = TraceFactory.eINSTANCE.createEMappingParameters();
        traceRecord.setParameters(eMappingParameters);
        for (EParameter param : mappingOperation.getEParameters()) {
            VarParameter varParameter = (VarParameter) param;
            VarParameterValue paramVPV = createVarParameterValue(mappingOperation, varParameter.getKind(),
                    varParameter.getEType(), varParameter.getName(), evalEnv);
            eMappingParameters.getParameters().add(paramVPV);
        }

        EMappingResults eMappingResults = TraceFactory.eINSTANCE.createEMappingResults();
        traceRecord.setResult(eMappingResults);

        EList<VarParameter> results = mappingOperation.getResult();
		if (!results.isEmpty()) {
            String resultVarName = results.size() == 1 ? results.get(0).getName() : Environment.RESULT_VARIABLE_NAME;
            EClassifier resultElementType = results.size() == 1 ? results.get(0).getEType() : mappingOperation.getEType();
			
            VarParameterValue resultVPV = createVarParameterValue(mappingOperation, DirectionKind.OUT, resultElementType, resultVarName, evalEnv);
            eMappingResults.getResult().add(resultVPV);
            EList<TraceRecord> resultMappings = createOrGetListElementFromMap(trace.getTargetToTraceRecordMap(), resultVPV.getValue().getOclObject());
            addUnique(traceRecord, resultMappings);
        }

		// Note: add it here so we ensure the record is fully initialized
        addUnique(traceRecord, trace.getTraceRecords());
        
        addTraceRecordByMapping(mappingOperation, traceRecord, trace);
        
        return traceRecord;
    }

	/**
	 * Improves performance a lot for mapping operations with parameters by
	 * means of (re)using the cache.
	 */
	private static void addTraceRecordByMapping(MappingOperation mappingOperation, TraceRecord traceRecord, Trace trace) {
		Object selfObj = null;
		if (traceRecord.getContext() != null && traceRecord.getContext().getContext() != null) {
			EValue value = traceRecord.getContext().getContext().getValue();
			selfObj = value.getOclObject();
		}
		Object key = createKey(selfObj, traceRecord);
		if (key != null) {
			trace.addRecordBySource(key, mappingOperation, traceRecord);
		}
	}

	/**
	 * Creates a key for mapping operations, based on the context (if available)
	 * and all parameters (if any).
	 */
	private static Object createKey(Object selfObj, QvtOperationalEvaluationEnv evalEnv, MappingOperation mappingOperation) {
		EList<EParameter> eParameters = mappingOperation.getEParameters();
		if (eParameters.isEmpty()) {
			// Backwards compatible for isParameterLessContextual
			return selfObj;
		}

		ArrayList<Object> key = new ArrayList<Object>(eParameters.size() + 1);
		key.add(selfObj);
		for (EParameter param : eParameters) {
			VarParameter varParam = (VarParameter) param;
			if (varParam.getKind() == DirectionKind.OUT) {
				continue;
			}
			key.add(evalEnv.getValueOf(param.getName()));
		}

		return key;
	}

	/**
	 * Creates a key for mapping operations, based on the context (if available)
	 * and all parameters (if any).
	 */
	private static Object createKey(Object selfObj, TraceRecord traceRecord) {
		EList<VarParameterValue> eParameters = traceRecord.getParameters().getParameters();
		if (eParameters.isEmpty()) {
			// Backwards compatible for isParameterLessContextual
			return selfObj;
		}

		ArrayList<Object> key = new ArrayList<Object>(eParameters.size() + 1);
		key.add(selfObj);
		for (VarParameterValue param : eParameters) {
			if (param.getKind() == EDirectionKind.OUT) {
				continue;
			}
			key.add(param.getValue().getOclObject());
		}

		return key;
	}

    static Object fetchResultFromTrace(QvtOperationalEvaluationEnv evalEnv, TraceRecord record) {
    	MappingOperation operation = record.getMappingOperation().getRuntimeMappingOperation();
    	if (operation.getResult().isEmpty()) {
            return null;
        }
    	
		ListIterator<Object> itArgument = evalEnv.getOperationArgs().listIterator();
		Iterator<VarParameterValue> itValues = record.getParameters().getParameters().iterator();
		while (itArgument.hasNext()) {
			VarParameterValue param = itValues.next();
			itArgument.next();
			if (param.getKind() == EDirectionKind.OUT) {
				itArgument.set(param.getValue().getOclObject());
			}
		}
    	
    	EList<VarParameterValue> traceResult = record.getResult().getResult();
    	assert traceResult.size() == 1;
		return traceResult.get(0).getValue().getOclObject();
    }
    
    
    private static VarParameterValue createVarParameterValue(MappingOperation mappingOperation, DirectionKind kind, EClassifier type, String name, QvtOperationalEvaluationEnv evalEnv) {
        VarParameterValue varParameterValue = TraceFactory.eINSTANCE.createVarParameterValue();
        varParameterValue.setKind(getDirectionKind(kind));
        varParameterValue.setName(name);
        varParameterValue.setType(type.getName());
        Object oclObject = evalEnv.getValueOf(name);
        varParameterValue.setValue(createEValue(oclObject, kind));
        return varParameterValue;
    }

    @SuppressWarnings("unchecked")
    public static EValue createEValue(Object oclObject, DirectionKind kind) {
        EValue value = TraceFactory.eINSTANCE.createEValue();
        value.setOclObject(cloneOclObject(oclObject, kind));
        if (oclObject != null) {
            if (oclObject instanceof Dictionary) {
            	Dictionary<Object, Object> dict = (Dictionary<Object, Object>) oclObject;
                value.setCollectionType(Dictionary.SINGLETON_NAME);
                for (Object dictKey : dict.keys()) {
                    ETuplePartValue tuplePartValue = TraceFactory.eINSTANCE.createETuplePartValue();
                    tuplePartValue.setName("key"); //$NON-NLS-1$
                    tuplePartValue.setValue(createEValue(dictKey, kind));
                    value.getCollection().add(tuplePartValue);

                    Object dictValue = dict.get(dictKey);
                    tuplePartValue = TraceFactory.eINSTANCE.createETuplePartValue();
                    tuplePartValue.setName("value"); //$NON-NLS-1$
                    tuplePartValue.setValue(createEValue(dictValue, kind));
                    value.getCollection().add(tuplePartValue);
                }
            } else if (oclObject instanceof Tuple) {
                Tuple<EOperation, EStructuralFeature> tuple = (Tuple<EOperation, EStructuralFeature>) oclObject;
                value.setCollectionType(TupleType.SINGLETON_NAME);
                TupleType<EOperation, EStructuralFeature> tupleType = tuple.getTupleType();
                for (EStructuralFeature part : tupleType.oclProperties()) {
                    Object partValue = tuple.getValue(part);
                    ETuplePartValue tuplePartValue = TraceFactory.eINSTANCE.createETuplePartValue();
                    tuplePartValue.setName(part.getName());
                    EValue partEValue = createEValue(partValue, kind);
                    tuplePartValue.setValue(partEValue);
                    value.getCollection().add(tuplePartValue);
                }
            } else if (oclObject instanceof Collection) {
                    Collection<Object> oclCollection = (Collection<Object>) oclObject;
                    // TODO: Write a test for checking collections
                    value.setCollectionType(getCollectionTypeName(oclCollection));
                    for (Object collectionElement : oclCollection) {
                        value.getCollection().add(createEValue(collectionElement, kind));
                    }
            } else if (oclObject instanceof ModelInstance) {
                value.setCollectionType(ModelType.SINGLETON_NAME);
                for (Object collectionElement : ((ModelInstance) oclObject).getExtent().getInitialObjects()) {
                    value.getCollection().add(createEValue(collectionElement, kind));
                }
            } else if (oclObject instanceof EObject) {
                value.setModelElement((EObject) oclObject);
            } else {
                if (oclObject != null) {
                    value.setPrimitiveValue(oclObject.toString());
                    value.setCollectionType(getPrimitiveTypeName(oclObject));
                }
            }
        }
        return value;
    }
    
    private static String getCollectionTypeName(Collection<?> c) {
    	String result = CollectionType.SINGLETON_NAME;
    	if (c instanceof MutableList<?>) {
    		result = MutableList.SINGLETON_NAME;
    	} else if (c instanceof Dictionary<?, ?>) {
    		result = Dictionary.SINGLETON_NAME;
    	} else if (c instanceof Bag<?>) {
    		result = BagType.SINGLETON_NAME;
    	} else if (c instanceof LinkedHashSet<?>) {
    		result = OrderedSetType.SINGLETON_NAME;
    	} else if (c instanceof Set<?>) {
    		result = SetType.SINGLETON_NAME;
    	} else if (c instanceof ArrayList<?>) {
    		result = SequenceType.SINGLETON_NAME;
    	}
    	return result;
    }

    private static String getPrimitiveTypeName(Object o) {
    	if (o instanceof Boolean) {
    		return PrimitiveType.BOOLEAN_NAME;
    	}
    	if (o instanceof Integer) {
    		return PrimitiveType.INTEGER_NAME;
    	}
    	if (o instanceof Double) {
    		return PrimitiveType.REAL_NAME;
    	}
    	if (o instanceof String) {
    		return PrimitiveType.STRING_NAME;
    	}
    	return null;
    }
    
	private static Object cloneOclObject(Object obj, DirectionKind kind) {
		return cloneOclObjectRec(obj, new IdentityHashMap<Object, Object>(), kind);
	}
	
    @SuppressWarnings("unchecked")
	private static Object cloneOclObjectRec(Object obj, Map<Object, Object> processed, DirectionKind kind) {
    	if (kind == DirectionKind.IN && obj instanceof MutableList<?>) {
        	if (processed.containsKey(obj)) {
        		return processed.get(obj);
        	}
        	
    		MutableList<Object> original = (MutableList<Object>) obj;
    		
    		MutableList<Object> result = Utils.createList();
    		processed.put(obj, result);

    		for (Object o : original) {
    			result.add(cloneOclObjectRec(o, processed, kind));
    		}    		
    		return result;
    	}

    	if (kind == DirectionKind.IN && obj instanceof Dictionary<?, ?>) {
        	if (processed.containsKey(obj)) {
        		return processed.get(obj);
        	}
        	
    		Dictionary<Object, Object> original = (Dictionary<Object, Object>) obj;
    		
    		Dictionary<Object, Object> result = Utils.createDictionary();
    		processed.put(obj, result);

    		for (Object k : original.keys()) {
    			result.put(cloneOclObjectRec(k, processed, kind), cloneOclObjectRec(original.get(k), processed, kind));
    		}    		
    		return result;
    	}
    	
    	return obj;
    }

    private static EDirectionKind getDirectionKind(DirectionKind kind) {
        if (kind == DirectionKind.IN) {
            return EDirectionKind.IN;
        } else if (kind == DirectionKind.INOUT) {
            return EDirectionKind.INOUT;
        } else if (kind == DirectionKind.OUT) {
            return EDirectionKind.OUT;
        }
        throw new RuntimeException("Wrong DirectionKind: " + kind.name()); //$NON-NLS-1$
    }
    
    private static <K, T> EList<T> createOrGetListElementFromMap(EMap<K, EList<T>> map, K key) {
        EList<T> list = map.get(key);
        if (list == null) {
            list = new BasicEList<T>();
            map.put(key, list);
            list = map.get(key);
        }
        return list;
    }

    private static boolean isOclEqual(Object candidateObject, Object traceObject, DirectionKind directionKind, QvtOperationalEvaluationEnv evalEnv) {
    	if (directionKind == DirectionKind.OUT) {
    		if (candidateObject == null) {
    			// yet not bound 'out' parameter, suit for any
    			return true;
    		}
    	}
        if (candidateObject == traceObject) {
            return true;
        }
        if (QvtOperationalUtil.isUndefined(candidateObject, evalEnv)) {
            return QvtOperationalUtil.isUndefined(traceObject, evalEnv);
        }
        if ((candidateObject == null) || (traceObject == null)) {
            return false;
        }
        return candidateObject.equals(traceObject); // Overridden equals() is implied
    }
    
    private static Boolean checkResultMatch(TraceRecord nextRecord, QvtOperationalEvaluationEnv evalEnv) {
        // check result parameters
        Object resultValue = evalEnv.getValueOf(Environment.RESULT_VARIABLE_NAME);
        if (resultValue != null) {
            List<Object> resultValues = new ArrayList<Object>(1); 
           	resultValues.add(resultValue);

            if (nextRecord.getResult().getResult().size() != resultValues.size()) {
                return null;
            }
            for (int i = 0, n = resultValues.size(); i < n; i++) {
                Object paramValue = resultValues.get(i);
                VarParameterValue traceParamVal = (VarParameterValue) nextRecord.getResult().getResult().get(i);
                if (!isOclEqual(paramValue, traceParamVal.getValue().getOclObject(), DirectionKind.OUT, evalEnv)) {
                    return Boolean.FALSE;
                }
            }
        }    	
        
        return Boolean.TRUE;
    }
    
	private static boolean isParameterLessContextual(MappingOperation mappingOperation) {
		return QvtOperationalParserUtil.isContextual(mappingOperation) && mappingOperation.getEParameters().isEmpty();
	}
	
    private static void addUnique(TraceRecord record, EList<TraceRecord> recordList) {
        if(recordList instanceof AbstractEList<?>) {
        	// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=287589 
        	AbstractEList<TraceRecord> basicRecList = (AbstractEList<TraceRecord>) recordList;
        	basicRecList.addUnique(record);
        } else {
        	// TODO - spit a trace warning
        	recordList.add(record);
        }    	
    }	
    
	static TraceRecord getIncrementalTraceRecord(QvtOperationalEvaluationEnv evalEnv, QvtOperationalEnv env, MappingOperation mappingOperation) {
		List<EObject> traceContent = evalEnv.getContext().getTrace().getTraceContent();
		if (traceContent.isEmpty()) {
			return null;
		}
		
		Object selfObj = evalEnv.getValueOf(Environment.SELF_VARIABLE_NAME);

		Object key = createKey(selfObj, evalEnv, mappingOperation);
		if (key != null) {
			for (EObject o : traceContent) {
				if (false == o instanceof Trace) {
					continue;
				}
				
				TraceRecord record = ((Trace) o).getRecordBySource(mappingOperation, key);
				if (record != null && Boolean.TRUE.equals(checkIncrementalResultMatch(env, record))) {
					return record;
				}
			}
		}

		// nothing found
		return null;
	}

    private static boolean checkIncrementalResultMatch(QvtOperationalEnv env, TraceRecord record) {
    	MappingOperation operation = record.getMappingOperation().getRuntimeMappingOperation();
    	
    	Iterator<VarParameterValue> itrRecordParams = record.getParameters().getParameters().iterator();
		for (EParameter param : operation.getEParameters()) {
			if (!itrRecordParams.hasNext()) {
				return false;
			}
			VarParameterValue recordParam = itrRecordParams.next();
			if (!recordParam.getType().equals(param.getEType().getName())) {
				return false;
			}
		}
		
		if (operation.getResult().isEmpty()) {
			return true;
		}
		if (record.getResult().getResult().size() != 1) {
			return false;
		}

		VarParameterValue resultParam = record.getResult().getResult().get(0);

		if (operation.getResult().size() == 1) {
			if (!resultParam.getType().equals(operation.getResult().get(0).getEType().getName())) {
				return false;
			}
		}
		else {
			EValue value = resultParam.getValue();
			if (!TupleType.SINGLETON_NAME.equals(value.getCollectionType())) {
				return false;
			}
			EClass resultType = (EClass) createOclTypeFromValue(env, value);
			for (VarParameter param : operation.getResult()) {
				EStructuralFeature feature = resultType.getEStructuralFeature(param.getName());
				if (feature == null) {
					return false;
				}
				if (!param.getEType().getName().equals(feature.getEType().getName())) {
					return false;
				}
			}
		}
		
		return true;
	}

	static void fetchIncrementalResultFromTrace(QvtOperationalEvaluationEnv evalEnv, TraceRecord record) {
    	MappingOperation operation = record.getMappingOperation().getRuntimeMappingOperation();
    	
		Iterator<EParameter> itParams = operation.getEParameters().iterator();
		for (VarParameterValue value : record.getParameters().getParameters()) {
			MappingParameter mappingParam = (MappingParameter) itParams.next();
			if (value.getKind() == EDirectionKind.OUT) {
				Object oclObject = value.getValue().getOclObject();
				if (oclObject != null) {
					evalEnv.replace(value.getName(), oclObject);
				}
				if (oclObject instanceof EObject) {
					evalEnv.putInstanceToExtent((EObject) oclObject, mappingParam.getExtent());
				}
			}
		}
		
		if (operation.getResult().isEmpty()) {
			return;
		}
    	
    	EList<VarParameterValue> traceResult = record.getResult().getResult();
    	assert traceResult.size() == 1;

    	if (operation.getResult().size() == 1) {
    		Object oclObject = traceResult.get(0).getValue().getOclObject();
    		if (oclObject != null) {
    			evalEnv.replace(Environment.RESULT_VARIABLE_NAME, oclObject);
    		}
			if (oclObject instanceof EObject) {
				MappingParameter resultParam = (MappingParameter) operation.getResult().get(0);
				evalEnv.putInstanceToExtent((EObject) oclObject, resultParam.getExtent());
			}
    	}
    	else if (operation.getResult().size() > 1) {
    		@SuppressWarnings("unchecked")
			Tuple<EOperation, EStructuralFeature> tuple = (Tuple<EOperation, EStructuralFeature>) traceResult.get(0).getValue().getOclObject();

	    	Iterator<VarParameter> itrResults = operation.getResult().iterator();
			TupleType<EOperation, EStructuralFeature> tupleType = tuple.getTupleType();
			for (EStructuralFeature feature : tupleType.oclProperties()) {
				Object oclObject = tuple.getValue(feature);
				if (oclObject != null) {
					evalEnv.replace(feature.getName(), oclObject);
				}
				if (oclObject instanceof EObject) {
					MappingParameter resultParam = (MappingParameter) itrResults.next();
					evalEnv.putInstanceToExtent((EObject) oclObject, resultParam.getExtent());
				}
			}
    	}
    }
    
    public static List<EObject> resolveTraces(QvtOperationalEnv env, Module qvtModule, List<EObject> traces) {
    	if (traces.isEmpty()) {
    		return traces;
    	}
    	
    	List<EObject> result = new ArrayList<EObject>(traces.size());
    	Map<String, MappingOperation> mappings = null;
    	
    	for (EObject o : traces) {
    		if (false == o instanceof Trace) {
    			result.add(o);
    			continue;
    		}
    		
    		Trace trace = (Trace) o;
        	if (!trace.hasRecordsBySource() || getTraceRootModule(trace) != qvtModule) {
            	if (mappings == null) {
            		mappings = new TreeMap<String, MappingOperation>();
                	fetchAllMappings(qvtModule, mappings);
            	}

        		setTraceRootModule(trace, qvtModule);
    			processTrace(env, trace, mappings);
        	}

       		result.add(trace);
    	}
    	
    	return result;
    }

    private static void processTrace(QvtOperationalEnv env, Trace trace, Map<String, MappingOperation> mappings) {
		trace.clearRecordsBySource();
    	
		for (TraceRecord traceRecord : trace.getTraceRecords()) {
			EMappingOperation eMappingOperation = traceRecord.getMappingOperation();
			MappingOperation mappingOperation = mappings.get(getMappingKey(eMappingOperation));
			eMappingOperation.setRuntimeMappingOperation(mappingOperation);
			
			if (mappingOperation != null) {
				if (traceRecord.getContext() != null && traceRecord.getContext().getContext() != null) {
					VarParameterValue value = traceRecord.getContext().getContext();
					value.getValue().setOclObject(createOclObjectFromValue(env, value.getValue(), value.getKind()));
				}

				EList<VarParameterValue> eParameters = traceRecord.getParameters().getParameters();
				for (VarParameterValue param : eParameters) {
					param.getValue().setOclObject(createOclObjectFromValue(env, param.getValue(), param.getKind()));
				}
				
				EList<VarParameterValue> eResults = traceRecord.getResult().getResult();
				for (VarParameterValue result : eResults) {
					result.getValue().setOclObject(createOclObjectFromValue(env, result.getValue(), result.getKind()));
				}
				
				addTraceRecordByMapping(mappingOperation, traceRecord, trace);
			}
		}

    	//EcoreUtil.resolveAll(trace);
	}

	private static Object createOclObjectFromValue(QvtOperationalEnv env, EValue value, EDirectionKind directionKind) {
		
		if (value.getModelElement() != null) {
			EObject modelElement = value.getModelElement();
			if (modelElement.eIsProxy()) {
				return null;
			}
			if (directionKind == EDirectionKind.OUT) {
				// model element which is fetched from the trace should operate like a new object
				((InternalEObject) modelElement).eSetResource(null, null);
			}
			return modelElement;
		}
		
		final String type = value.getCollectionType();

		if (value.getPrimitiveValue() != null) {
			final String primitiveValue = value.getPrimitiveValue();
			
			if (PrimitiveType.STRING_NAME.equals(type)) {
				return primitiveValue;
			}
			else if (PrimitiveType.BOOLEAN_NAME.equals(type)) {
				return Boolean.valueOf(primitiveValue);
			}
			else if (PrimitiveType.INTEGER_NAME.equals(type)) {
				return Integer.valueOf(primitiveValue);
			}
			else if (PrimitiveType.REAL_NAME.equals(type)) {
				return Double.valueOf(primitiveValue);
			}
			else if (PrimitiveType.UNLIMITED_NATURAL_NAME.equals(type)) {
				return Integer.valueOf(primitiveValue);
			}
			assert false : ("Unknown primitive type: " + type); //$NON-NLS-1$
		}
		
		if (Dictionary.SINGLETON_NAME.equals(type)) {
			Dictionary<Object, Object> dict = Utils.createDictionary();
			
			for (Iterator<EValue> itr = value.getCollection().iterator(); itr.hasNext(); ) {
				ETuplePartValue key = (ETuplePartValue) itr.next();
				assert "key".equals(key.getName());
				
				ETuplePartValue val = (ETuplePartValue) itr.next();
				assert "value".equals(val.getName());
				
				
				dict.put(createOclObjectFromValue(env, key.getValue(), directionKind), createOclObjectFromValue(env, val.getValue(), directionKind));
			}
			
			return dict;
		}

		if (TupleType.SINGLETON_NAME.equals(type)) {
			List<Variable<EClassifier, EParameter>> parts = new ArrayList<Variable<EClassifier,EParameter>>(value.getCollection().size());		
			for (EValue elem : value.getCollection()) {
				ETuplePartValue part = (ETuplePartValue) elem;
				Variable<EClassifier, EParameter> var = env.getOCLFactory().createVariable();
				var.setName(part.getName());
				var.setType(createOclTypeFromValue(env, part.getValue()));
				parts.add(var);
			}		

			EClassifier tupleType = env.getTypeResolver().resolve((EClassifier)env.getOCLFactory().createTupleType(parts));			
			EObject tuple = TupleFactory.createTuple((EClass) tupleType);
			
			for (EValue elem : value.getCollection()) {
				ETuplePartValue part = (ETuplePartValue) elem;
				
				EStructuralFeature feature = tuple.eClass().getEStructuralFeature(part.getName());
				tuple.eSet(feature, createOclObjectFromValue(env, part.getValue(), directionKind));
			}
			
			return tuple;
		}
		
		Collection<Object> oclCollection = null;
		
		if (MutableList.SINGLETON_NAME.equals(type)) {
			oclCollection = Utils.createList();
		}
		else if (BagType.SINGLETON_NAME.equals(type)) {
			oclCollection = CollectionUtil.createNewBag();
		}
		else if (OrderedSetType.SINGLETON_NAME.equals(type)) {
			oclCollection = CollectionUtil.createNewOrderedSet();
		}
		else if (SetType.SINGLETON_NAME.equals(type)) {
			oclCollection = CollectionUtil.createNewSet();
		}
		else if (SequenceType.SINGLETON_NAME.equals(type)) {
			oclCollection = CollectionUtil.createNewSequence();
		}
		
		if (oclCollection != null) {
			for (EValue elem : value.getCollection()) {
				oclCollection.add(createOclObjectFromValue(env, elem, directionKind));
			}
			return oclCollection;
		}
		
		assert false : ("Unsupported type: " + type); //$NON-NLS-1$

		return null;
	}

	private static EClassifier createOclTypeFromValue(QvtOperationalEnv env, EValue value) {
		if (value.getModelElement() != null) {
			return value.getModelElement().eClass();
		}
		
		final String type = value.getCollectionType();

		if (value.getPrimitiveValue() != null) {
			if (PrimitiveType.STRING_NAME.equals(type)) {
				return env.getOCLStandardLibrary().getString();
			}
			else if (PrimitiveType.BOOLEAN_NAME.equals(type)) {
				return env.getOCLStandardLibrary().getBoolean();
			}
			else if (PrimitiveType.INTEGER_NAME.equals(type)) {
				return env.getOCLStandardLibrary().getInteger();
			}
			else if (PrimitiveType.REAL_NAME.equals(type)) {
				return env.getOCLStandardLibrary().getReal();
			}
			else if (PrimitiveType.UNLIMITED_NATURAL_NAME.equals(type)) {
				return env.getOCLStandardLibrary().getUnlimitedNatural();
			}
			assert false : ("Unknown primitive type: " + type); //$NON-NLS-1$
		}
		
		if (Dictionary.SINGLETON_NAME.equals(type)) {
			if (value.getCollection().isEmpty()) {
				return env.getQVTStandardLibrary().getDictionary();
			}
			else {
				for (Iterator<EValue> itr = value.getCollection().iterator(); itr.hasNext(); ) {
					ETuplePartValue key = (ETuplePartValue) itr.next();
					assert "key".equals(key.getName());
					
					ETuplePartValue val = (ETuplePartValue) itr.next();
					assert "value".equals(val.getName());
					
					return env.getTypeResolver().resolveDictionaryType(createOclTypeFromValue(env, key.getValue()), createOclTypeFromValue(env, val.getValue()));
				}
			}
		}

		if (TupleType.SINGLETON_NAME.equals(type)) {
			List<Variable<EClassifier, EParameter>> parts = new ArrayList<Variable<EClassifier,EParameter>>(value.getCollection().size());		
			for (EValue elem : value.getCollection()) {
				ETuplePartValue part = (ETuplePartValue) elem;
				Variable<EClassifier, EParameter> var = env.getOCLFactory().createVariable();
				var.setName(part.getName());
				var.setType(createOclTypeFromValue(env, part.getValue()));
				parts.add(var);
			}
			return env.getTypeResolver().resolve((EClassifier)env.getOCLFactory().createTupleType(parts));			
		}
		
		if (MutableList.SINGLETON_NAME.equals(type)) {
			if (value.getCollection().isEmpty()) {
				return env.getQVTStandardLibrary().getList();
			}
			else {
				return env.getTypeResolver().resolveListType(createOclTypeFromValue(env, value.getCollection().iterator().next()));
			}
		}
		else if (BagType.SINGLETON_NAME.equals(type)) {
			if (value.getCollection().isEmpty()) {
				return env.getOCLStandardLibrary().getBag();
			}
			else {
				return TypeUtil.resolveBagType(env, createOclTypeFromValue(env, value.getCollection().iterator().next()));
			}
		}
		else if (OrderedSetType.SINGLETON_NAME.equals(type)) {
			if (value.getCollection().isEmpty()) {
				return env.getOCLStandardLibrary().getOrderedSet();
			}
			else {
				return TypeUtil.resolveOrderedSetType(env, createOclTypeFromValue(env, value.getCollection().iterator().next()));
			}
		}
		else if (SetType.SINGLETON_NAME.equals(type)) {
			if (value.getCollection().isEmpty()) {
				return env.getOCLStandardLibrary().getSet();
			}
			else {
				return TypeUtil.resolveSetType(env, createOclTypeFromValue(env, value.getCollection().iterator().next()));
			}
		}
		else if (SequenceType.SINGLETON_NAME.equals(type)) {
			if (value.getCollection().isEmpty()) {
				return env.getOCLStandardLibrary().getSequence();
			}
			else {
				return TypeUtil.resolveSequenceType(env, createOclTypeFromValue(env, value.getCollection().iterator().next()));
			}
		}
		
		assert false : ("Unsupported type: " + type); //$NON-NLS-1$

		return null;
	}

	private static void fetchAllMappings(Module qvtModule, Map<String, MappingOperation> mappings) {
    	for (EOperation op : qvtModule.getEOperations()) {
    		if (op instanceof MappingOperation) {
    			MappingOperation mappingOperation = (MappingOperation) op;
    			mappings.put(getMappingKey(mappingOperation), mappingOperation);
    		}
    	}
    	for (ModuleImport mi : qvtModule.getModuleImport()) {
    		fetchAllMappings(mi.getImportedModule(), mappings);
    	}
    }
    
    private static String getMappingKey(MappingOperation mappingOperation) {
        String result = mappingOperation.getName();
        result += '#';
        Module module = QvtOperationalParserUtil.getOwningModule(mappingOperation);
        result += module.getNsPrefix();
        result += '#';
        result += module.getName();
        return result;
    }

    private static String getMappingKey(EMappingOperation eMappingOperation) {
        String result = eMappingOperation.getName();
        result += '#';
        result += eMappingOperation.getPackage();
        result += '#';
        result += eMappingOperation.getModule();
        return result;
    }

	public static void setTraceRootModule(QvtOperationalEvaluationEnv evalEnv, Module module) {
		InternalEvaluationEnv internEnv = evalEnv.getAdapter(InternalEvaluationEnv.class);
		Trace trace = internEnv.getTraces();
		
		setTraceRootModule(trace, module);
	}

	private static void setTraceRootModule(Trace trace, Module module) {
		Adapter adapter = EcoreUtil.getAdapter(trace.eAdapters(), TraceModuleAdapter.class);
		if(adapter != null) {
			trace.eAdapters().remove(adapter);
		}
		trace.eAdapters().add(new TraceModuleAdapter(module));
	}

	private static Module getTraceRootModule(Trace trace) {
		Adapter adapter = EcoreUtil.getAdapter(trace.eAdapters(), TraceModuleAdapter.class);
		if(adapter instanceof TraceModuleAdapter) {
			return ((TraceModuleAdapter) adapter).getModule();
		}
		return null;
	}


	private static class TraceModuleAdapter extends AdapterImpl {

		private final Module fModule;

		TraceModuleAdapter(Module module) {
			assert module != null;
			fModule = module;
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type == TraceModuleAdapter.class;
		}
		
		Module getModule() {
			return fModule;
		}
	}
	
}
