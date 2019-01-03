package com.svenj.converterfastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by svenj on 2019/1/2
 */
public final class FastJsonConverterFactory extends Converter.Factory {
    // config for FastJson
    private ParserConfig parserConfig;
    private int featureValues;
    private Feature[] features;
    private SerializeConfig serializeConfig;
    private SerializerFeature[] serializerFeatures;

    public static FastJsonConverterFactory create() {
        return create(new Builder());
    }

    public static FastJsonConverterFactory create(Builder builder) {
        return builder.build();
    }

    private FastJsonConverterFactory() {
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type, parserConfig, featureValues, features);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations,
            Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>(serializeConfig, serializerFeatures);
    }

    public static class Builder {
        private ParserConfig parserConfig;
        private int featureValues;
        private Feature[] features;
        private SerializeConfig serializeConfig;
        private SerializerFeature[] serializerFeatures;

        public Builder() {
            parserConfig = ParserConfig.getGlobalInstance();
            featureValues = JSON.DEFAULT_PARSER_FEATURE;
            serializeConfig = null;
            serializerFeatures = null;
        }

        public Builder setParserConfig(ParserConfig config) {
            this.parserConfig = config;
            return this;
        }

        public Builder setFeatureValues(int featureValues) {
            this.featureValues = featureValues;
            return this;
        }

        public Builder setFeatures(Feature[] features) {
            this.features = features;
            return this;
        }

        public Builder setSerializeConfig(SerializeConfig serializeConfig) {
            this.serializeConfig = serializeConfig;
            return this;
        }

        public Builder setSerializerFeatures(SerializerFeature[] serializerFeatures) {
            this.serializerFeatures = serializerFeatures;
            return this;
        }

        public FastJsonConverterFactory build() {
            FastJsonConverterFactory factory = new FastJsonConverterFactory();
            factory.parserConfig = this.parserConfig;
            factory.features = this.features;
            factory.featureValues = this.featureValues;
            factory.serializeConfig = this.serializeConfig;
            factory.serializerFeatures = this.serializerFeatures;
            return factory;
        }
    }
}
