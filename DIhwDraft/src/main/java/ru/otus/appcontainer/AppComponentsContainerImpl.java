package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.api.ComponentCreationException;

import java.lang.reflect.Method;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {
  /**
   * Судя по всему, считаем что order == 0, указывает на отсутствие зависимостей
   */
  private static final int ROOT_ORDER = 0;
  
  private final List<Object> appComponents = new ArrayList<>();
  private final Map<String, Object> appComponentsByName = new HashMap<>();
  
  public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception {
    processConfig(initialConfigClass);
  }
  
  private void processConfig(Class<?> configClass) throws Exception {
    checkConfigClass(configClass);
    final var methods = configClass.getDeclaredMethods();
    final var instance = configClass.getConstructors()[0].newInstance();
    
    Arrays.sort(methods, Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()));
    
    for (Method method : methods) {
      if (method.isAnnotationPresent(AppComponent.class)) {
        final var annotation = method.getAnnotation(AppComponent.class);
        
        List<Object> dependencies = new ArrayList<>();
        if (annotation.order() != ROOT_ORDER)
          dependencies = this.getDependencies(method);
        
        final var invokedObject = method.invoke(instance, dependencies.toArray());
        
        if (invokedObject != null) {
          appComponents.add(invokedObject);
          appComponentsByName.put(annotation.name(), invokedObject);
        }
      }
    }
  }
  
  @Override
  public <C> C getAppComponent(Class<C> componentClass) {
    for (Object appComponent : appComponents) {
      if (componentClass.isInstance(appComponent)){
        //noinspection unchecked
        return (C) appComponent;
      }
    }
    
    return null;
  }
  
  @Override
  public <C> C getAppComponent(String componentName) {
    if (appComponentsByName.containsKey(componentName)) {
      //noinspection unchecked
      return (C) appComponentsByName.get(componentName);
    }
    
    return null;
  }
  
  private List<Object> getDependencies(Method method) throws ComponentCreationException {
    final var parameterTypes = method.getParameterTypes();
    var dependencies = new ArrayList<>();
    
    for (Class<?> parameterType : parameterTypes) {
      final var dependency = this.getAppComponent(parameterType);
      
      if (dependency == null)
        throw new ComponentCreationException("App container doesn't contain object with type " + parameterType);
      
      dependencies.add(dependency);
    }
    
    return dependencies;
  }
  
  private void checkConfigClass(Class<?> configClass) {
    if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
      throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
    }
  }
}
