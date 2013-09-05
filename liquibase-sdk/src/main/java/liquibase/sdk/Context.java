package liquibase.sdk;

import liquibase.change.Change;
import liquibase.change.ChangeFactory;
import liquibase.sdk.exception.UnexpectedLiquibaseSdkException;
import liquibase.sqlgenerator.SqlGenerator;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

public class Context {

    private static Context instance;
    private static final List<Class<?>> extensionInterfaces = Arrays.asList(Change.class, SqlGenerator.class);



    private boolean initialized = false;
    private Set<String> packages = new HashSet<String>();
    private Set<Class> allClasses = new HashSet<Class>();
    private Map<Class, Set<Class>> seenExtensionClasses = new HashMap<Class, Set<Class>>();

    private Context() {
    }

    public static void reset() {
        instance = null;
    }

    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Set<String> getPackages() {
        return packages;
    }

    public Set<Class> getAllClasses() {
        return allClasses;
    }

    public Map<Class, Set<Class>> getSeenExtensionClasses() {
        return seenExtensionClasses;
    }

    public void init(Set<String> packages) {
        this.packages = packages;
        try {
            for (String packageName : packages) {
                Enumeration<URL> dirs = this.getClass().getClassLoader().getResources(packageName.replace('.', '/'));
                while (dirs.hasMoreElements()) {
                    File dir = new File(dirs.nextElement().toURI());
                    findClasses(packageName, dir);
                }
            }
        } catch (Exception e) {
            throw new UnexpectedLiquibaseSdkException(e);
        }

        for (Class clazz : allClasses) {
            if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
                continue;
            }
            Class type = getExtensionType(clazz);
            if (type != null) {
                if (!seenExtensionClasses.containsKey(type)) {
                    seenExtensionClasses.put(type, new HashSet<Class>());
                }
                seenExtensionClasses.get(type).add(clazz);
            }
        }

        this.initialized = true;
    }

    private Class getExtensionType(Class clazz) {
        for (Class type : clazz.getInterfaces()) {
            if (extensionInterfaces.contains(type)) {
                return type;
            }
        }
        Class superclass = clazz.getSuperclass();
        if (superclass == null) {
            return null;
        }
        return getExtensionType(superclass);
    }

    private void findClasses(String packageName, File dir) throws ClassNotFoundException {
        String[] classFiles = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".class");
            }
        });
        for (String classFile : classFiles) {
            Class<?> foundClass = Class.forName(packageName + "." + classFile.replaceFirst("\\.class$", ""));
            allClasses.add(foundClass);
        }

        File[] subDirs = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File subDir : subDirs) {
            findClasses(packageName+"."+subDir.getName(), subDir);
        }

    }
}