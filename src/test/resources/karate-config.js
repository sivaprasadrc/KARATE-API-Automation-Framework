function confugFn(){
    var config  = {};
    karate.configure('abortSuiteOnFailure', true);
    config.usrnm = "customer";
    config.pwd = "customer";
    config.loginHeaders = {
        'accept' : 'application/json',
        'content-type' : 'application/json'
    };
    config.commonHeaders = {
        'accept' : 'application/json',
        'content-type' : 'text/plain',
        'x-sk' : 'zrDKs5gyWyIFxJLmLVnmj149ciHaa5CfJfbTP1unccHQUw2eICBfEfh+WIUalAt+u5z/BYQJjw85Gpka6FJX0mI0U5CNlIbc0GXBiAbatdU3OUoP9e0YHxNCVoVrI4gDd0woHGCNF2FJ+r3Ky2P73Uuo958kQh0sflNc7A6Ihm4/WYhiNzaWr1eSNVfFoDgaSayEiaPzmA+zPK6Mq5DKnR3XR4xP72/p2BSR/c7+Ge7NMkNIYcoRP1mKzazkcOejW8b1wt6QlPVvj/OppuXUpQiEsc+XdivrpAJf+yH47yNuDWNlbhbH3um/HnyhSEbjHSrRRUwafBrQ7mwr9Ya9ejnWsSdqTiceShA+O1XqpOX5vp3iTexyOHy2w26v1OrNIxhJ2Z4tRli/U1MCGkJV+5Ur/zP18UNkyI6OMvIM2X/bkFgB/4ARYXFF3wcxn0fpYsVVmgh/whWOICVFBhmRSXmNb2Oa0IFslPMBIv19RESZZ1JGJqpQLownkdAGhxlU'
    };
    config.baseUrl = 'https://campaign-gnc-uat.ezeefin.net.in/consumerAPI/v3';
    config.loginPath = ['auth','login'];
    config.flowPath = ['application','consumer','save_or_update'];
    config.flowParams = {
        isV2Flow:true,
        lang:'en'
    };
    config.secretKey = 'a8d35aebc7f5f98fedaeb79af69a20daf5bf4d6bba70fc47fdbd13d4888e3090';


    // config.secretKey = karate.properties['api.secretKey']
    //     || karate.properties['API_SK']
    //     || java.lang.System.getenv('API_SK');
    //
    // if (!config.secretKey) {
    //     karate.fail('API_SK environment variable is not configured');
    // }



    return config;
}