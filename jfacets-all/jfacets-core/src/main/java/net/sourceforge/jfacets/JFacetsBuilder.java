package net.sourceforge.jfacets;

import net.sourceforge.jfacets.impl.FacetRepositoryImpl;
import net.sourceforge.jfacets.impl.DefaultFacetFactory;
import net.sourceforge.jfacets.impl.DefaultFacetContextFactory;
import net.sourceforge.jfacets.log.JFacetsLogger;

/**
 * Builder that eases creation and wiring of the JFacets bean when not relying on an
 * external IoC container.
 *
 * Typical usage looks like this :
 * <br/>
 * <pre>
 *         jFacets = new JFacetsBuilder(
 *              new SimpleProfileRepository(),
 *              new FacetDescriptorManager("/test-instance-facets.xml")).
 *              build();
 * </pre>
 * <br/>
 */
public class JFacetsBuilder {

    private static final JFacetsLogger logger = JFacetsLogger.getLogger(JFacetsBuilder.class);

    private IProfileRepository profileRepository;
    private IFacetDescriptorManager facetDescriptorManager;
    private IFacetFactory facetFactory = new DefaultFacetFactory();
    private IFacetContextFactory facetContextFactory = new DefaultFacetContextFactory();
    private String fallbackProfileId;
    private boolean useProfilesCache = false;


    /**
     * Create the builder with mandatory parameters
     * @param profileRepository the profile repository to be used
     * @param facetDescriptorManager the facet descriptor manager to be used
     */
    public JFacetsBuilder(IProfileRepository profileRepository, IFacetDescriptorManager facetDescriptorManager) {
        this.profileRepository = profileRepository;
        this.facetDescriptorManager = facetDescriptorManager;
    }

    /**
     * Set the facet factory (defaults to {@link net.sourceforge.jfacets.impl.DefaultFacetFactory}
     * @param facetFactory the Facet Factory to be used
     * @return the builder, for chained call
     */
    public JFacetsBuilder setFacetFactory(IFacetFactory facetFactory) {
        this.facetFactory = facetFactory;
        return this;
    }

    /**
     * Set the facet context factory (defaults to {@link net.sourceforge.jfacets.impl.DefaultFacetContextFactory}
     * @param facetContextFactory the Facet Context Factory to be used
     * @return the builder, for chained call
     */
    public JFacetsBuilder setFacetContextFactory(IFacetContextFactory facetContextFactory) {
        this.facetContextFactory = facetContextFactory;
        return this;
    }

    /**
     * Builds JFacets
     * @return the JFacets bean, ready for use
     */
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


    /**
     * Set the fallback profile ID
     * @param fallbackProfileId the fallback profile ID
     * @return the builder, for chained call
     */
    public JFacetsBuilder setFallbackProfileId(String fallbackProfileId) {
        this.fallbackProfileId = fallbackProfileId;
        return this;
    }

    /**
     * Use cache for profiles
     * @param useProfilesCache <code>true</code> if you want to use the built-in profiles cache, <code>false</code> otherwise
     * @return the builder, for chained call
     */
    public JFacetsBuilder setUseProfilesCache(boolean useProfilesCache) {
        this.useProfilesCache = useProfilesCache;
        return this;
    }
}
