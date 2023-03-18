package com.example.loginJwtRSA.utils;

import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.stereotype.Component;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

@Component
public class CommonTest {
    public static OperationPreprocessor removeHeadersRequest() {
        return removeHeaders(
                "Host",
                "Content-Length"
        );
    }

    public static OperationPreprocessor removeHeadersResponse() {
        return removeHeaders(
                "Date",
                "Transfer-Encoding",
                "Keep-Alive",
                "Connection",
                "Content-Length"
        );
    }

    public static HeaderDescriptor[] createRequestHeadersWithoutAuthorization() {
        return new HeaderDescriptor[] {
                headerWithName("Content-Type")
                        .description("The content type of the payload")
                        .attributes(key("example").value("application/json;charset=UTF-8"))
        };
    }

    public static HeaderDescriptor[] createRequestHeadersCommon() {
        return new HeaderDescriptor[] {
                headerWithName("Content-Type")
                        .description("The content type of the payload")
                        .attributes(key("example").value("application/json;charset=UTF-8")),
                headerWithName("Authorization")
                        .description("Json Web Token for authorization")
                        .attributes(key("example").value("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjc4NjIyODMzLCJleHAiOjE2Nzg3MDkyMzN9.CmiZeCJn1cZBehZ8SRcFNgovhvwgLuXjeS61i2RFdYc"))
        };
    }

    public static ParameterDescriptor createPathParameter(
            String name,
            Object explanation,
            boolean option,
            Object restriction,
            Object sample) {
        ParameterDescriptor parameter = parameterWithName(name)
                .description(explanation);
        if (option == true) parameter.optional();
        if (restriction != null) parameter.attributes(key("constraints").value(restriction));
        if (sample != null) parameter.attributes(key("example").value(sample));

        return parameter;
    }

    public static ParameterDescriptor createRequestParameter(
            String name,
            Object explanation,
            boolean option,
            Object restriction,
            Object sample) {
        ParameterDescriptor parameter = parameterWithName(name)
                .description(explanation);
        if (option == true) parameter.optional();
        if (restriction != null) parameter.attributes(key("constraints").value(restriction));
        if (sample != null) parameter.attributes(key("example").value(sample));

        return parameter;
    }

    public static FieldDescriptor createRequestFields(
            String path,
            JsonFieldType jsonFieldType,
            Object explanation,
            boolean option,
            Object restriction,
            Object sample) {
        FieldDescriptor field = fieldWithPath(path)
                .type(jsonFieldType)
                .description(explanation);
        if (option == true) field.optional();
        if (restriction != null) field.attributes(key("constraints").value(restriction));
        if (sample != null) field.attributes(key("example").value(sample));

        return field;
    }

    public static FieldDescriptor createResponseFields(
            String path,
            JsonFieldType jsonFieldType,
            Object explanation,
            boolean option,
            Object restriction,
            Object sample) {
        FieldDescriptor field = fieldWithPath(path)
                .type(jsonFieldType)
                .description(explanation);
        if (option == true) field.optional();
        if (restriction != null) field.attributes(key("constraints").value(restriction));
        if (sample != null) field.attributes(key("example").value(sample));

        return field;
    }
}
