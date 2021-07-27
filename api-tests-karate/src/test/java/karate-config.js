function fn() {

    var env = karate.env;
    karate.log('api.karate.env system property was:', env);

    if (!env) {
        env = 'system';
    }

    var config = {
        env: env,
        status: '/health',
        menu: '/v1/menu',
        menu_v2: '/v2/menu',
        items: '/items',

        generate_auth0_token: false,

        base_url: java.lang.System.getenv('BASE_URL'),
        client_id_value: java.lang.System.getenv('CLIENT_ID'),
        client_secret_value: java.lang.System.getenv('CLIENT_SECRET'),
        audience_value: java.lang.System.getenv('AUDIENCE'),
        grant_type_value: java.lang.System.getenv('GRANT_TYPE'),
        oauth_token_url: java.lang.System.getenv('OAUTH_TOKEN_URL'),

        menu_by_id_path: '/v1/menu/<menu_id>',
        category: '/v1/menu/<menu_id>/category',
        category_by_id_path: '/v1/menu/<menu_id>/category/<category_id>',
        item_by_id_path: '/v1/menu/<menu_id>/category/<category_id>/items/<item_id>',

        menu_already_exists: 'A Menu with the name \'<menu_name>\' already exists for the restaurant with id \'<tenant_id>\'.',
        menu_does_not_exists: 'A menu with id \'<menu_id>\' does not exist.',
        category_does_not_exists: 'A category with the id \'<category_id>\' does not exist for menu with id \'<menu_id>\'.',
        category_already_exists: 'A category with the name \'<category_name>\' already exists for the menu with id \'<menu_id>\'.',
        item_already_exists: 'An item with the name \'<item_name>\' already exists for the category \'<category_id>\' in menu with id \'<menu_id>\'.',
        item_does_not_exists: 'An item with the id \'<item_id>\' does not exists for category with the id \'<category_id>\' and for menu with id \'<menu_id>\'.',
    };


    if (env == 'local') {
        config.base_url = 'http://localhost:9000';
    } else if (env == 'prod') {
        config.base_url = 'https://prod-java-api.prod.amidostacks.com/api/menu';
    } else if (env == 'system') {
        config.base_url = java.lang.System.getenv('BASE_URL');
    }

    if (config.generate_auth0_token) {
        var result = karate.callSingle('classpath:GenerateOauthToken.feature', config);
        config.auth = {bearer_token: result.token_type.concat(' ').concat(result.token)};
    } else {
        config.auth = {bearer_token: null}
    }

    karate.callSingle('classpath:CleanUpTestData.feature', config);

    var oauth_body = {
        "client_id": "",
        "client_secret": "",
        "audience": "",
        "grant_type": ""
    };

    var menu_body = {
        "tenantId": "",
        "name": "",
        "description": "",
        "enabled": null
    };

    var category_body = {
        "name": "",
        "description": ""
    };

    var item_body = {
        "name": "",
        "description": "",
        "price": 0,
        "available": null
    };

    return config;
}
