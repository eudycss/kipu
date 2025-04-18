package com.seibe.auth.infrastructure.config;

// This configuration is currently disabled to prevent circular dependency
// between Flyway and EntityManagerFactory during testing
// Rename back to FlywayConfig when migrations are needed again
//@Configuration
public class DisabledFlywayConfig {

    private final javax.sql.DataSource dataSource;

    //@Autowired
    public DisabledFlywayConfig(javax.sql.DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /*
     * Flyway migration bean is disabled to prevent problems when running the application.
     * Uncomment and add proper dependencies when database migrations are needed.
     * 
    //@Bean(initMethod = "migrate")
    public org.flywaydb.core.Flyway flyway() {
        return org.flywaydb.core.Flyway.configure()
            .dataSource(dataSource)
            .baselineOnMigrate(true)
            .locations("classpath:db/migration")
            .load();
    }
    */
} 