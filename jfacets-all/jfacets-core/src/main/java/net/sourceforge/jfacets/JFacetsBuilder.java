package net.sourceforge.jfacets;

import net.sourceforge.jfacets.impl.FacetRepositoryImpl;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * Builder that eases creation and wiring of the JFacets bean when not relying on an
 * external IoC container. Typical usage looks like this :
 * <br/>
 * <pre>JFacetsBuilder builder = new JFacetsBuilder();
        jFacets = builder.setFacetContextFactory(new DefaultFacetContextFactory()).
                setFacetDescriptorManager(new FacetDescriptorManager("/test-instance-facets.xml")).
                setFacetFactory(new DefaultFacetFactory()).
                setProfileRepository(new SimpleProfileRepository()).
                build();</pre>
 * <br/>
 */
public class JFacetsBuilder {

    private static final JFacetsLogger logger = JFacetsLogger.getLogger(JFacetsBuilder.class);

    private IProfileRepository profileRepository;
    private IFacetDescriptorManager facetDescriptorManager;
    private IFacetFactory facetFactory;
    private IFacetContextFactory facetContextFactory;
    private String fallbackProfileId;
    private boolean useProfilesCache = false;

    public JFacetsBuilder setProfileRepository(IProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        return this;
    }

    public JFacetsBuilder setFacetDescriptorManager(IFacetDescriptorManager facetDescriptorManager) {
        this.facetDescriptorManager = facetDescriptorManager;
        return this;
    }

    public JFacetsBuilder setFacetFactory(IFacetFactory facetFactory) {
        this.facetFactory = facetFactory;
        return this;
    }

    public JFacetsBuilder setFacetContextFactory(IFacetContextFactory facetContextFactory) {
        this.facetContextFactory = facetContextFactory;
        return this;
    }

    public JFacets build() {
        FacetRepositoryImpl facetRepository = new FacetRepositoryImpl(profileRepository, facetFactory, facetContextFactory, facetDescriptorManager);
        JFacets jFacets = new JFacets();
        jFacets.setFacetRepository(facetRepository);
        if (fallbackProfileId!=null) {
            jFacets.setFallbackProfileId(fallbackProfileId);
        }
        jFacets.setUseProfilesCache(useProfilesCache);
        try {
            jFacets.afterPropertiesSet();
        } catch(Exception e) {
            logger.error("Exception caught while initializing the JFacets bean !", e);
            jFacets = null;
        }
        return jFacets;
    }


    public JFacetsBuilder setFallbackProfileId(String fallbackProfileId) {
        this.fallbackProfileId = fallbackProfileId;
        return this;
    }

    public JFacetsBuilder setUseProfilesCache(boolean useProfilesCache) {
        this.useProfilesCache = useProfilesCache;
        return this;
    }
}
