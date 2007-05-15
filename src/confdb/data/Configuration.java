package confdb.data;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Configuration
 * -------------
 * @author Philipp Schieferdecker
 *
 * A complete CMSSW (hlt-)job configuration.
 */
public class Configuration
{
    //
    // member data
    //

    /** configuration information */
    private ConfigInfo configInfo = null;
    
    /** has the configuration changed since the last 'save' operation? */
    private boolean hasChanged = false;
    
    /** edsource template hash map */
    private HashMap<String,Template> edsourceTemplateHashMap = null;

    /** essource template hash map */
    private HashMap<String,Template> essourceTemplateHashMap = null;

    /** service template hash map */
    private HashMap<String,Template> serviceTemplateHashMap  = null;

    /** module template hash map */
    private HashMap<String,Template> moduleTemplateHashMap   = null;
    
    /** list of globale parameter sets */
    private ArrayList<PSetParameter> psets = null;

    /** list of EDSources */
    private ArrayList<EDSourceInstance> edsources = null;

    /** list of ESSources */
    private ArrayList<ESSourceInstance> essources = null;
    
    /** list of Services */
    private ArrayList<ServiceInstance>  services = null;
    
    /** list of Modules */
    private ArrayList<ModuleInstance> modules = null;
    
    /** list of Paths */
    private ArrayList<Path> paths = null;
    
    /** list of Sequences */
    private ArrayList<Sequence> sequences = null;
    
    
    //
    // construction
    //
    
    /** empty constructor */
    public Configuration()
    {
	edsourceTemplateHashMap = new HashMap<String,Template>();
	essourceTemplateHashMap = new HashMap<String,Template>();
	serviceTemplateHashMap  = new HashMap<String,Template>();
	moduleTemplateHashMap   = new HashMap<String,Template>();
	
	psets     = new ArrayList<PSetParameter>();
	edsources = new ArrayList<EDSourceInstance>();
	essources = new ArrayList<ESSourceInstance>();
	services  = new ArrayList<ServiceInstance>();
	modules   = new ArrayList<ModuleInstance>();
	paths     = new ArrayList<Path>();
	sequences = new ArrayList<Sequence>();
    }
    
    /** standard constructor */
    public Configuration(ConfigInfo configInfo,
			 ArrayList<Template> edsourceTemplateList,
			 ArrayList<Template> essourceTemplateList,
			 ArrayList<Template> serviceTemplateList,
			 ArrayList<Template> moduleTemplateList)
    {
	edsourceTemplateHashMap = new HashMap<String,Template>();
	essourceTemplateHashMap = new HashMap<String,Template>();
	serviceTemplateHashMap  = new HashMap<String,Template>();
	moduleTemplateHashMap   = new HashMap<String,Template>();
	
	psets     = new ArrayList<PSetParameter>();
	edsources = new ArrayList<EDSourceInstance>();
	essources = new ArrayList<ESSourceInstance>();
	services  = new ArrayList<ServiceInstance>();
	modules   = new ArrayList<ModuleInstance>();
	paths     = new ArrayList<Path>();
	sequences = new ArrayList<Sequence>();
	
	initialize(configInfo,
		   edsourceTemplateList,
		   essourceTemplateList,
		   serviceTemplateList,
		   moduleTemplateList);
    }
    
    
    //
    // public member functions
    //

    /** new configuration*/
    public void initialize(ConfigInfo configInfo,
			   ArrayList<Template> edsourceTemplateList,
			   ArrayList<Template> essourceTemplateList,
			   ArrayList<Template> serviceTemplateList,
			   ArrayList<Template> moduleTemplateList)
    {
	this.configInfo = configInfo;
	
	setHasChanged(false);
	
	updateHashMaps(edsourceTemplateList,
		       essourceTemplateList,
		       serviceTemplateList,
		       moduleTemplateList);

	psets.clear();
	edsources.clear();
	essources.clear();
	services.clear();
	modules.clear();
	paths.clear();
	sequences.clear();
    }

    /** update template hash maps */
    public void updateHashMaps(ArrayList<Template> edsourceTemplateList,
			       ArrayList<Template> essourceTemplateList,
			       ArrayList<Template> serviceTemplateList,
			       ArrayList<Template> moduleTemplateList)
    {
	edsourceTemplateHashMap.clear();
	essourceTemplateHashMap.clear();
	serviceTemplateHashMap.clear();
	moduleTemplateHashMap.clear();
	
	for (Template t : edsourceTemplateList)
	    edsourceTemplateHashMap.put(t.name(),t);
	for (Template t : essourceTemplateList)
	    essourceTemplateHashMap.put(t.name(),t);
	for (Template t : serviceTemplateList)
	    serviceTemplateHashMap.put(t.name(),t);
	for (Template t : moduleTemplateList)
	    moduleTemplateHashMap.put(t.name(),t);
    }

    /** reset configuration */
    public void reset()
    { 
	configInfo = null;
	
	setHasChanged(false);
	
	edsourceTemplateHashMap.clear();
	essourceTemplateHashMap.clear();
	serviceTemplateHashMap.clear();
	moduleTemplateHashMap.clear();

	psets.clear();
	edsources.clear();
	essources.clear();
	services.clear();
	modules.clear();
	paths.clear();
	sequences.clear();
    }
    
    /** set the configuration info */
    public void setConfigInfo(ConfigInfo configInfo)
    {
	if (!configInfo.releaseTag().equals(releaseTag())) {
	    System.out.println("Configuration.setConfigInfo ERROR: "+
			       "releaseTag mismatch (" +
			       releaseTag() + " / " +
			       configInfo.releaseTag() + ")");
	}
	this.configInfo = configInfo;
    }

    /** overlaod toString() */
    public String toString()
    {
	String result=new String();
	if (configInfo==null) return result;
	if (parentDir()!=null) result += parentDir().name();
	if (result.length()!=1) result += "/";
	result += name() + ", Version " + version();
	return result;
    }
    
    /** isEmpty() */
    public boolean isEmpty()
    {
	return (name().length()==0&&psets.isEmpty()&&
		edsources.isEmpty()&&essources.isEmpty()&&
		services.isEmpty()&&modules.isEmpty()&&
		paths.isEmpty()&&sequences.isEmpty());
    }

    /** database identifier */
    public int dbId()
    {
	return (configInfo!=null) ? configInfo.dbId() : -1;
    }

    /** get configuration name */
    public String name()
    {
	return (configInfo!=null) ? configInfo.name() : "";
    }
    
    /** get parent directory */
    public Directory parentDir()
    {
	return (configInfo!=null) ? configInfo.parentDir() : null;
    }

    /** get parent directory database id */
    public int parentDirId() 
    {
	return (parentDir()!=null) ? parentDir().dbId() : 0;
    }
    
    /** get configuration version */
    public int version()
    {
	return (configInfo!=null) ? configInfo.version() : 0;
    }
    
    /** next version */
    public int nextVersion()
    {
	return (configInfo!=null) ? configInfo.nextVersion() : 0;
    }
    
    /** add the next version */
    public void addNextVersion(int versionId,String created,String releaseTag)
    {
	configInfo.addVersion(versionId,nextVersion(),created,releaseTag);
	configInfo.setVersionIndex(0);
    }
    
    /** get configuration data of creation as a string */
    public String created()
    {
	return (configInfo!=null) ? configInfo.created() : "";
    }
    
    /** get release tag this configuration is associated with */
    public String releaseTag()
    {
	return (configInfo!=null) ? configInfo.releaseTag() : "";
    }
    
    /** indicate if configuration must be saved */
    public boolean hasChanged() { return hasChanged; }
    
    /** set the 'hasChanged' flag */
    public void setHasChanged(boolean hasChanged) { this.hasChanged = hasChanged; }


    //
    // unset tracked parameter counts
    //

    /** total number of unset tracked parameters */
    public int unsetTrackedParameterCount()
    {
	int result = 0;
	result += unsetTrackedPSetParameterCount();
	result += unsetTrackedEDSourceParameterCount();
	result += unsetTrackedESSourceParameterCount();
	result += unsetTrackedServiceParameterCount();
	result += unsetTrackedModuleParameterCount();
	return result;
    }

    /** number of unsert tracked global pset parameters */
    public int unsetTrackedPSetParameterCount()
    {
	int result = 0;
	for (PSetParameter pset : psets) {
	    for (int i=0;i<pset.parameterCount();i++) {
		Parameter p = pset.parameter(i);
		if (p.isTracked()&&!p.isValueSet()) result++;
	    }
	}
	return result;
    }
    
    /** number of unsert tracked edsource parameters */
    public int unsetTrackedEDSourceParameterCount()
    {
	int result = 0;
	for (EDSourceInstance eds : edsources)
	    result+=eds.unsetTrackedParameterCount();
	return result;
    }

    /** number of unsert tracked essource parameters */
    public int unsetTrackedESSourceParameterCount()
    {
	int result = 0;
	for (ESSourceInstance ess : essources)
	    result+=ess.unsetTrackedParameterCount();
	return result;
    }

    /** number of unsert tracked service parameters */
    public int unsetTrackedServiceParameterCount()
    {
	int result = 0;
	for (ServiceInstance svc : services)
	    result+=svc.unsetTrackedParameterCount();
	return result;
    }

    /** number of unsert tracked module parameters */
    public int unsetTrackedModuleParameterCount()
    {
	int result = 0;
	for (ModuleInstance mod : modules)
	    result+=mod.unsetTrackedParameterCount();
	return result;
    }


    //
    // PSets
    //
    
    /**  number of global PSets */
    public int psetCount() { return psets.size(); }

    /** get i-th global PSet */
    public PSetParameter pset(int i) { return psets.get(i); }

    /** index of a certain global PSet */
    public int indexOfPSet(PSetParameter pset)
    {
	return psets.indexOf(pset);
    }
    
    /** insert global pset at i-th position */
    public void insertPSet(PSetParameter pset)
    {
	psets.add(pset);
	hasChanged = true;
    }
    
    /** remove a global PSet */
    public void removePSet(PSetParameter pset)
    {
	int index = psets.indexOf(pset);
	psets.remove(pset);
	hasChanged = true;
    }
    


    //
    // EDSources 
    //
    
    /**  number of EDSources */
    public int edsourceCount() { return edsources.size(); }

    /** get i-th EDSource */
    public EDSourceInstance edsource(int i) { return edsources.get(i); }

    /** index of a certain EDSource */
    public int indexOfEDSource(EDSourceInstance edsource)
    {
	return edsources.indexOf(edsource);
    }
    
    /** insert EDSource at i-th position */
    public EDSourceInstance insertEDSource(String templateName)
    {
	if (edsourceCount()>0) return null;
	EDSourceTemplate template =
	    (EDSourceTemplate)edsourceTemplateHashMap.get(templateName);
	EDSourceInstance instance = null;
	try {
	    instance = (EDSourceInstance)template.instance();
	    edsources.add(instance);
	    hasChanged = true;
	}
	catch (DataException e) {
	    System.out.println(e.getMessage());
	}
	return instance;
    }
    
    /** remove a EDSource */
    public void removeEDSource(EDSourceInstance edsource)
    {
	edsource.remove();
	int index = edsources.indexOf(edsource);
	edsources.remove(index);
	hasChanged = true;
    }
    

    //
    // ESSources
    //
    
    /**  number of ESSources */
    public int essourceCount() { return essources.size(); }
    
    /** get i-th ESSource */
    public ESSourceInstance essource(int i) { return essources.get(i); }

    /** index of a certain ESSource */
    public int indexOfESSource(ESSourceInstance essource)
    {
	return essources.indexOf(essource);
    }
    
    /** insert ESSource at i=th position */
    public ESSourceInstance insertESSource(int i,
					   String templateName,String instanceName)
    {
	ESSourceTemplate template =
	    (ESSourceTemplate)essourceTemplateHashMap.get(templateName);
	ESSourceInstance instance = null;
	try {
	    instance = (ESSourceInstance)template.instance(instanceName);
	    essources.add(i,instance);
	    hasChanged = true;
	}
	catch (DataException e) {
	    System.out.println(e.getMessage());
	}
	return instance;
    }
    
    /** remove a ESSource */
    public void removeESSource(ESSourceInstance essource)
    {
	essource.remove();
	int index = essources.indexOf(essource);
	essources.remove(index);
	hasChanged = true;
    }
    
    
    //
    // Services
    //
    
    /**  number of Services */
    public int serviceCount() { return services.size(); }

    /** get i-th Service */
    public ServiceInstance service(int i) { return services.get(i); }

    /** index of a certain Service */
    public int indexOfService(ServiceInstance service)
    {
	return services.indexOf(service);
    }
    
    /** insert Service at i=th position */
    public ServiceInstance insertService(int i,String templateName)
    {
	ServiceTemplate template =
	    (ServiceTemplate)serviceTemplateHashMap.get(templateName);
	ServiceInstance instance = null;
	try {
	    instance = (ServiceInstance)template.instance();
	    services.add(i,instance);
	    hasChanged = true;
	}
	catch (DataException e) {
	    System.out.println(e.getMessage());
	}
	return instance;
    }
    
    /** remove a Service */
    public void removeService(ServiceInstance service)
    {
	service.remove();
	int index = services.indexOf(service);
	services.remove(index);
	hasChanged = true;
    }
    
    
    //
    // Modules 
    //
    
    /**  number of Modules */
    public int moduleCount() { return modules.size(); }

    /** get i-th Module */
    public ModuleInstance module(int i) { return modules.get(i); }
    
    /** index of a certain Module */
    public int indexOfModule(ModuleInstance module)
    {
	return modules.indexOf(module);
    }
    
    /** get module template by name, from hash map */
    public ModuleTemplate moduleTemplate(String templateName)
    {
	return (ModuleTemplate)moduleTemplateHashMap.get(templateName);
    }

    /** insert a module */
    public ModuleInstance insertModule(String templateName,String instanceName)
    {
	ModuleTemplate  template =
	    (ModuleTemplate)moduleTemplateHashMap.get(templateName);
	ModuleInstance  instance  = null;
	try {
	    instance = (ModuleInstance)template.instance(instanceName);
	}
	catch (DataException e) { System.out.println(e.getMessage()); }
	if (instance.referenceCount()==0) {
	    modules.add(instance);
	    hasChanged = true;
	}
	return instance;
    }
    
    /** remove a module reference */
    public void removeModuleReference(ModuleReference module)
    {
	ModuleInstance instance = (ModuleInstance)module.parent();
	module.remove();
	if (instance.referenceCount()==0) {
	    int index = modules.indexOf(instance);
	    modules.remove(index);
	}
	hasChanged = true;
    }
    
    
    /** insert ModuleReference at i-th position into a path/sequence */
    public ModuleReference insertModuleReference(ReferenceContainer container,
						 int                i,
						 ModuleInstance     instance)
    {
	ModuleReference reference =
	    (ModuleReference)instance.createReference(container,i);
	hasChanged = true;
	return reference;
    }
    
    /** insert ModuleReference at i-th position into a path/sequence */
    public ModuleReference insertModuleReference(ReferenceContainer container,
						 int                i,
						 String             templateName,
						 String             instanceName)
    {
	ModuleInstance instance = insertModule(templateName,instanceName);
	return insertModuleReference(container,i,instance);
    }    
    

    //
    // Paths
    //

    /** number of Paths */
    public int pathCount() { return paths.size(); }
    
    /** get i-th Path */
    public Path path(int i) { return paths.get(i); }

    /** index of a certain Path */
    public int indexOfPath(Path path)
    {
	return paths.indexOf(path);
    }
    
    /** insert path at i-th position */
    public Path insertPath(int i, String pathName)
    {
	Path path = new Path(pathName);
	paths.add(i,path);
	hasChanged = true;
	return path;
    }
    
    /** get the sequence number of a certain path */
    public int pathSequenceNb(Path path) { return paths.indexOf(path); }
    
    /** remove a path */
    public void removePath(Path path)
    {
	while (path.referenceCount()>0) {
	    PathReference reference = (PathReference)path.reference(0);
	    reference.remove();
	}
	
	// remove all entries of this path
	while (path.entryCount()>0) {
	    Reference reference = path.entry(0);
	    reference.remove();
	    if (reference instanceof ModuleReference) {
		ModuleReference module   = (ModuleReference)reference;
		ModuleInstance  instance = (ModuleInstance)module.parent();
		if (instance.referenceCount()==0) {
		    int index = modules.indexOf(instance);
		    modules.remove(index);
		}
	    }
	}
	
	int index = paths.indexOf(path);
	paths.remove(index);
	hasChanged = true;
    }
    
    /** insert a path reference into another path */
    public PathReference insertPathReference(Path parentPath,int i,Path path)
    {
	PathReference reference = (PathReference)path.createReference(parentPath,i);
	hasChanged = true;
	return reference;
    }
    
    
    //
    // Sequences
    //

    /** number of Sequences */
    public int sequenceCount() { return sequences.size(); }
    
    /** get i-th Sequence */
    public Sequence sequence(int i) { return sequences.get(i); }

    /** index of a certain Sequence */
    public int indexOfSequence(Sequence sequence)
    {
	return sequences.indexOf(sequence);
    }
    
    /** insert sequence */
    public Sequence insertSequence(int i,String sequenceName)
    {
	Sequence sequence = new Sequence(sequenceName);
	sequences.add(i,sequence);
	hasChanged = true;
	return sequence;
    }
    
    /** remove a sequence */
    public void removeSequence(Sequence sequence)
    {
	while (sequence.referenceCount()>0) {
	    SequenceReference reference = (SequenceReference)sequence.reference(0);
	    reference.remove();
	}
	
	// remove all modules from this sequence
	while (sequence.entryCount()>0) {
	    ModuleReference reference = (ModuleReference)sequence.entry(0);
	    ModuleInstance  instance  = (ModuleInstance)reference.parent();
	    reference.remove();
	    if (instance.referenceCount()==0) {
		int index = modules.indexOf(instance);
		modules.remove(index);
	    }
	}

	int index = sequences.indexOf(sequence);
	sequences.remove(index);
	hasChanged = true;
    }
    
    /** insert a sequence reference into another path */
    public SequenceReference insertSequenceReference(Path parentPath,int i,
						     Sequence sequence)
    {
	SequenceReference reference =
	    (SequenceReference)sequence.createReference(parentPath,i);
	hasChanged = true;
	return reference;
    }
    
}
