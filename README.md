# converter-fastjson
使用FastJson解析Retrofit返回结果的库

### 使用
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
