/* Copyright (C) 2005-2006 Aaron Porter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.mongus.servlet.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContext;

import com.mongus.util.Log;


/**
 * <p>ClassLoader provides an easy way to find classes that extend classes,
 * implement interfaces, or are annotated with specific annotations. The search
 * may be limited to specific packages.
 * 
 * @author Aaron Porter
 *
 * @param <T> The type of classes ClassLoader will return
 */
public class ClassLocator<T>
{
	private Set<Class<T>> classes = new HashSet<Class<T>>();
	
	private Set<Class<? extends T>> types = new HashSet<Class<? extends T>>();
	
	private Set<String> packages = new HashSet<String>();
	
	private Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
	
	private ServletContext context;
	
	private static final Log log = Log.getInstance(ClassLocator.class);
	
	public ClassLocator(ServletContext context)
	{
		this.context = context;
	}
	
	/**
	 * Scan all classes in /WEB-INF/classes/ and all jar files in /WEB-INF/lib/
	 * for classes that are in specific packages (when set) that are castable to
	 * one of the specified classes/interfaces OR are annotated with one of the
	 * specified annotations. 
	 * 
	 * @return All classes that matched the criteria
	 */
	@SuppressWarnings("unchecked")
	public Set<Class<T>> findClasses()
	{
		if (packages.size() == 0)
			log.warn("No packages specified! Scanning all classes in all packages. This could take a while.");

		addClassesFromContext("/WEB-INF/classes/");

		Set<String> jars = context.getResourcePaths("/WEB-INF/lib/");

		if (jars != null)
		{
			for (String jarName : jars)
			{
				if (!jarName.endsWith(".jar"))
					continue;

				try
				{
					addClassesFromJar(new JarInputStream(context.getResourceAsStream(jarName)));
				} catch (IOException e)
				{
					log.warn("Couldn't create JarInputStream for ", jarName);
				}
			}
		}
		
		return classes;
	}

	protected void addClassesFromJar(JarInputStream stream)
	{
		JarEntry entry;

		try
		{
			while ((entry = stream.getNextJarEntry()) != null)
			{
				if (entry.isDirectory())
					continue;

				String name = entry.getName();

				if (name.endsWith(".class"))
					addIfMatches(name, true);
			}
		} catch (IOException e)
		{
			log.error(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected void addClassesFromContext(String path)
	{
		Set<String> paths = context.getResourcePaths(path);

		if (paths != null)
		{
			for (String subPath : paths)
			{
				if (subPath.endsWith("/"))
					addClassesFromContext(subPath);
				else if (subPath.endsWith(".class"))
					addIfMatches(subPath.replace("/WEB-INF/classes/", "").replace('/', '.').replaceAll("\\.class$", ""), true);
			}
		}
	}

	/**
	 * Check the class specified by FQN and add it to the list of classes if it matches
	 * the specified criteria.
	 * 
	 * @param name FQN of class to match
	 * @return true if the class matched the specified criteria
	 */
	public boolean addIfMatches(String name)
	{
		return addIfMatches(name, false);
	}

	@SuppressWarnings("unchecked")
	private boolean addIfMatches(String name, boolean checkPackage)
	{
		name = name.replace('/', '.');
		if (name.endsWith(".class"))
			name = name.substring(0, name.length() - 6);

		if ((packages.size()) > 0 && checkPackage)
		{
			boolean matched = false;

			for (String p : packages)
			{
				if (name.startsWith(p))
				{
					matched = true;
					break;
				}
			}

			if (!matched)
				return false;
		}

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try
		{
			Class<T> c = (Class<T>) loader.loadClass(name);
			
			boolean matched = false;
			
			// Check annotations
			for (Class<? extends Annotation> annotation : annotations)
				if (c.isAnnotationPresent(annotation))
				{
					matched = true;
					break;
				}

			if (!matched)
				for (Class<? extends T> type : types)
					if (type.isAssignableFrom(c))
					{
						matched = true;
						break;
					}
			
			if (matched)
			{
				classes.add((Class<T>) c);
				log.trace("Added ", name, " to class list");
				
				return true;
			}
		} catch (ClassNotFoundException e)
		{
			log.warn("Could not load ", name);
		}

		return false;
	}

	public Set<Class<? extends Annotation>> getAnnotations()
	{
		return annotations;
	}

	public void setAnnotations(Set<Class<? extends Annotation>> annotations)
	{
		this.annotations = annotations;
	}
	
	public void addAnnotation(Class<? extends Annotation> annotation)
	{
		annotations.add(annotation);
	}
	
	public void addAnnotations(Class<? extends Annotation>... annotations)
	{
		for (Class<? extends Annotation> annotation : annotations)
			addAnnotation(annotation);
	}
	
	public void addAnnotations(Collection<Class<? extends Annotation>> annotations)
	{
		for (Class<? extends Annotation> annotation : annotations)
			addAnnotation(annotation);
	}

	public Set<Class<T>> getClasses()
	{
		return classes;
	}

	public void setClasses(Set<Class<T>> classes)
	{
		this.classes = classes;
	}

	public Set<String> getPackages()
	{
		return packages;
	}

	public void setPackages(Set<String> packages)
	{
		this.packages = packages;
	}
	
	public void addPackage(String p)
	{
		packages.add(p);
	}
	
	public void addPackages(String... packages)
	{
		for (String p : packages)
			addPackage(p);
	}
	
	public void addPackages(Collection<String> packages)
	{
		for (String p : packages)
			addPackage(p);
	}

	public Set<Class<? extends T>> getTypes()
	{
		return types;
	}

	public void setTypes(Set<Class<? extends T>> types)
	{
		this.types = types;
	}
	
	public void addType(Class<? extends T> type)
	{
		types.add(type);
	}

	public void addTypes(Class<? extends T>... types)
	{
		for (Class<? extends T> type : types)
			addType(type);
	}
	
	public void addTypes(Collection<Class<? extends T>> types)
	{
		for (Class<? extends T> type : types)
			addType(type);
	}
}
