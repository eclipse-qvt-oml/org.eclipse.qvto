/*******************************************************************************
 * Copyright (c) 2007, 2018 Borland Software Corporation and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *     Borland Software Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.internal.qvt.oml.project.nature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

public class NatureUtils {
    private NatureUtils() {}
    
    public static boolean hasNature(IProjectDescription description,String id) throws CoreException{
        String natureIds[] = description.getNatureIds();
        for(int i=0;i<natureIds.length;i++){
            if(id.equals(natureIds[i])){
                return true;
            }
        }
        return false;
    }
    
    public static void addNature(IProject project, String id) throws CoreException{
//        IProjectDescription description = project.getDescription();
//        if(addNature(description, id)) {
//            project.setDescription(description, null);
//        }
    	
    	IProjectDescription description = project.getDescription();
		String[] prevNatures = description.getNatureIds();
		String[] newNatures = new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length] = id;
		description.setNatureIds(newNatures);
		project.setDescription(description, null);
    }
    
    public static boolean addNature(IProjectDescription description, String id) throws CoreException{
        String[] natures = description.getNatureIds();
        for(int i=0;i<natures.length;i++){
            if(id.equals(natures[i])){
                return false;
            }
        }
        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 1, natures.length);
        newNatures[0] = id;
        description.setNatureIds(newNatures);
        return true;
    }

    public static void removeNature(IProjectDescription description, String id) throws CoreException{
        String[] natures = description.getNatureIds();
        for(int i=0;i<natures.length;i++){
            if(id.equals(natures[i])){
                String[] newNatures = new String[natures.length - 1];
                System.arraycopy(natures, 0, newNatures, 0, i);
                System.arraycopy(natures, i+1 , newNatures,i,natures.length-i-1);
                description.setNatureIds(newNatures);
                return;
            }
        }
    }
    
    public static boolean addBuilders(IProjectDescription pd,String[] builderIds,String[] insertBefore) {
        ICommand[] commands = pd.getBuildSpec();
        List<ICommand> newCommandsList = new ArrayList<ICommand>(Arrays.asList(commands));
        int index = newCommandsList.size()-1;
        if(index==-1){
            index = 0;
        }
        for(int i=0;i<insertBefore.length;i++){
            ICommand command = findCommand(commands,insertBefore[i]);
            if(command!=null){
                int temp = newCommandsList.indexOf(command);
                if(temp!=-1){
                    index = Math.min(temp,index);
                }
            }
        }
        
        for(int i=0;i<builderIds.length;i++){
            ICommand builderCommand = findCommand(commands,builderIds[i]);
            if(builderCommand==null){
                builderCommand = pd.newCommand();
                builderCommand.setBuilderName(builderIds[i]);
                newCommandsList.add(index,builderCommand);
                index++;
            }
        }
        
        if(newCommandsList.size()!=commands.length){
            ICommand[] newCommands = (ICommand[])newCommandsList.toArray(new ICommand[newCommandsList.size()]);
            pd.setBuildSpec(newCommands);
            return true;
        }
        return false;
    }

    public static boolean isBuilderEnabled(IProject project,String builderId) throws CoreException {
        if(project == null){
            return false;
        }
        return findCommand(project.getDescription().getBuildSpec(),builderId)!=null;
    }
    
    public static ICommand findCommand(IProject project, String builderId) throws CoreException {
        return findCommand(project.getDescription().getBuildSpec(), builderId);
    }
    
    public static ICommand findCommand(ICommand[] commands,String builderId){
        for (int i = 0; i < commands.length; ++i) {
            if (builderId.equals(commands[i].getBuilderName())) {
                return commands[i]; // already set
            }
        }
        return null;
    }
    
    public static void updateCommand(IProject project, ICommand command) throws CoreException {
        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();

        boolean updated = false;
        for (int i = 0; i < commands.length; ++i) {
            if (command.getBuilderName().equals(commands[i].getBuilderName())) {
                commands[i] = command;
                updated = true;
            }
        }
        
        if(updated) {
            desc.setBuildSpec(commands);
            project.setDescription(desc, null);
        }
        
        // TODO Auto-generated method stub
        
    }
    
    public static boolean removeBuilders(IProjectDescription pd,String[] builderIds) {
        ICommand[] commands = pd.getBuildSpec();
        List<ICommand> newCommandsList = new ArrayList<ICommand>(Arrays.asList(commands));
        
        for(int i=0;i<builderIds.length;i++){
            ICommand builderCommand = findCommand(commands,builderIds[i]);
            if(builderCommand!=null){
                newCommandsList.remove(builderCommand);
            }
        }
        
        if(newCommandsList.size()!=commands.length){
            ICommand[] newCommands = (ICommand[])newCommandsList.toArray(new ICommand[newCommandsList.size()]); 
            pd.setBuildSpec(newCommands);
            return true;
        }
        return false;
    }
}
