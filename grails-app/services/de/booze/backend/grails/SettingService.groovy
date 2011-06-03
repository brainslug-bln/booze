package de.booze.backend.grails

class SettingService {

  static transactional = true

  def getDeviceDrivers(String packageName) throws ClassNotFoundException, IOException {

    List myDriverClasses = getClasses(packageName)
    def drivers = []
    myDriverClasses.each {
      drivers.add(it.packageName + "." + it.className)
    }
      
    return drivers
  }
  
  def bindMotorTasks(Setting setting, Map params, List thingsToRemove) {
    boolean validationErrors = false
    
    ['mashingPump', 'mashingMixer', 'cookingPump', 'cookingMixer', 'drainPump'].each() { it ->

      thingsToRemove.add(setting[it])
      thingsToRemove.add(setting[it]?.mode)
        
      if(params.setting[it]?.active == "true") {
        setting[it] = new MotorTask()
        setting[it].properties = params.setting[it]
        setting[it].mode = new MotorDeviceMode()
        setting[it].mode.properties = params.setting[it]?.mode
        setting[it].setting = setting
        
        if (!setting[it].mode.validate() || !setting[it].validate()) {
          validationErrors = true
        }
      }
      else {
        setting[it] = null
      }
      
      params.setting.remove(it)
    }
    
    return validationErrors
  }
    

    
  /**
   * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
   *
   * @param packageName The base package
   * @return The classes
   * @throws ClassNotFoundException
   * @throws IOException
   */
  private List getClasses(String packageName) throws ClassNotFoundException, IOException {
    //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    //assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = this.getClass().getClassLoader().getResources(path);
    //Enumeration<URL> resources = classLoader.getResources(path);
    List<File> dirs = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      dirs.add(new File(resource.getFile()));
    }
    ArrayList<Class> classes = new ArrayList<Class>();
    for (File directory: dirs) {
      classes.addAll(findClasses(directory, packageName));
    }
    return classes.toList();
  }
    
  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory The base directory
   * @param packageName The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
    List<Class> classes = new ArrayList<Class>();
    if (!directory.exists()) {
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file: files) {
      if (file.isDirectory()) {
        if (file.getName().contains(".")) {
          return classes;
        }
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add([packageName: packageName, className: file.getName().substring(0, file.getName().length() - 6)]);
      }
    }
    return classes;
  }
}
