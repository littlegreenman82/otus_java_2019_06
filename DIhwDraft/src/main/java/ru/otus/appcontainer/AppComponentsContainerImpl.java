package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.api.ComponentCreationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();
    
    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws ComponentCreationException {
        processConfig(initialConfigClass);
    }
    
    private void processConfig(Class<?> configClass) throws ComponentCreationException {
        try {
            checkConfigClass(configClass);
            final var methods = this.getConfigClassMethods(configClass);
            final Object instance = configClass.getConstructors()[0].newInstance();
            final var annotatedMethodsOrdered = Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                    .collect(Collectors.toList());
            
            for (Method method : annotatedMethodsOrdered) {
                Object[] dependencies = this.getDependencies(method);
                final var annotation = method.getAnnotation(AppComponent.class);
                final var invokedObject = method.invoke(instance, dependencies);
                
                if (invokedObject != null) {
                    appComponents.add(invokedObject);
                    appComponentsByName.put(annotation.name(), invokedObject);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new ComponentCreationException("Unable create instance of config class");
        }
    }
    
    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        for (Object appComponent : appComponents) {
            if (componentClass.isInstance(appComponent)) {
                //noinspection unchecked
                return (C) appComponent;
            }
        }
        
        return null;
    }
    
    @Override
    public <C> C getAppComponent(String componentName) {
        //noinspection unchecked
        return (C) appComponentsByName.get(componentName);
    }
    
    private Method[] getConfigClassMethods(Class<?> configClass) {
        return configClass.getDeclaredMethods();
    }
    
    private Object[] getDependencies(Method method) throws ComponentCreationException {
        final var parameterTypes = method.getParameterTypes();
        var dependencies = new Object[parameterTypes.length];
        
        for (int i = 0; i < parameterTypes.length; i++) {
            final var dependency = this.getAppComponent(parameterTypes[i]);
            
            if (dependency == null) {
                throw new ComponentCreationException("App container doesn't contain object with type " + parameterTypes[i]);
            }
            
            dependencies[i] = dependency;
        }
        
        return dependencies;
    }
    
    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }
}
