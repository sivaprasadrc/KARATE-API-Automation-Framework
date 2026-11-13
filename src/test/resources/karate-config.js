function fn() {
    karate.configure('abortSuiteOnFailure', true);
    var config = {};
    config.usrnm = java.lang.System.getenv('API_USERNAME');
    config.pwd = java.lang.System.getenv('API_PASSWORD');
    config.secretKey = java.lang.System.getenv('API_SECRET_KEY');
    config.API_X_SK = java.lang.System.getenv('API_X_SK');
    if (!config.usrnm || !config.pwd || !config.secretKey || !config.API_X_SK) {
        throw new Error('Missing required environment variables: API_USERNAME, API_PASSWORD, API_SECRET_KEY or API_X_SK');
    }
    var env = karate.env || 'uat';
    var supportedEnvironments = ['uat', 'prod'];
    if (!supportedEnvironments.includes(env)) {
        throw new Error('Unsupported environment: ' + env + '. Supported environments: ' + supportedEnvironments.join(', '));
    }
    var envConfig = karate.read('classpath:apiConfig/envConfig/' + env + '.json');
    var headersConfig = karate.read('classpath:apiConfig/headersConfig/flowHeaders.json');
    var pathConfig = karate.read('classpath:apiConfig/pathConfig/flowPath.json');
    config = karate.merge(config, envConfig);
    config = karate.merge(config, headersConfig);
    config = karate.merge(config, pathConfig);
    if (env === 'uat' && !config.baseUrl.toLowerCase().includes('uat')) {
        throw new Error('Invalid UAT URL. Expected URL containing "uat".');
    }
    if (env === 'prod' &&
        java.lang.System.getProperty('karate.confirmProd') !== 'true') {
        throw new Error(
            'PRODUCTION execution blocked. Use -Dkarate.confirmProd=true with your command to proceed.'
        );
    }

    config.commonHeaders['x-sk'] = config.API_X_SK;
    return config;


}
