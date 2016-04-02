package jerseywiremock.annotations.handler.requestmatching.paramdescriptors;

import jerseywiremock.annotations.ParamFormat;
import jerseywiremock.annotations.ParamMatchedBy;
import jerseywiremock.annotations.ParamMatchingStrategy;
import jerseywiremock.annotations.formatter.ParamFormatter;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class ParameterAnnotationsProcessor {
    public LinkedList<ParameterDescriptor> createParameterDescriptors(Method targetMethod, Method mockerMethod) {
        LinkedList<ParameterDescriptor> parameterDescriptors = new LinkedList<>();
        Annotation[][] targetMethodParameterAnnotations = targetMethod.getParameterAnnotations();
        Annotation[][] mockerMethodParameterAnnotations = mockerMethod.getParameterAnnotations();

        int mockerMethodParamIndex = 0;
        for (Annotation[] targetSingleParamAnnotations : targetMethodParameterAnnotations) {
            if (!includesQueryOrPathParams(targetSingleParamAnnotations)) {
                continue;
            }

            if (mockerMethodParamIndex >= mockerMethodParameterAnnotations.length) {
                throw new RuntimeException("Expected " + mockerMethod.getName() + " to have at least " +
                        (mockerMethodParamIndex+1) + " params, but has " + mockerMethodParameterAnnotations.length);
            }

            Annotation[] mockerSingleParamAnnotations = mockerMethodParameterAnnotations[mockerMethodParamIndex];

            ParameterDescriptor parameterDescriptor =
                    getParameterDescriptor(targetSingleParamAnnotations, mockerSingleParamAnnotations);
            parameterDescriptors.add(parameterDescriptor);

            mockerMethodParamIndex++;
        }
        return parameterDescriptors;
    }

    private boolean includesQueryOrPathParams(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam || annotation instanceof PathParam) {
                return true;
            }
        }
        return false;
    }

    private ParameterDescriptor getParameterDescriptor(
            Annotation[] targetParamAnnotations,
            Annotation[] mockerParamAnnotations
    ) {
        String paramName = getParamName(targetParamAnnotations);
        Class<? extends ParamFormatter> formatter = getParamFormatter(targetParamAnnotations);
        ParamType paramType = getParamType(targetParamAnnotations);

        // Param matching strategies do not make sense for path params, so are ignored
        ParamMatchingStrategy matchingStrategy = null;
        if (paramType == ParamType.QUERY) {
            matchingStrategy = getParamMatchingStrategy(mockerParamAnnotations);
        }

        return new ParameterDescriptor(paramType, paramName, formatter, matchingStrategy);
    }

    private String getParamName(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                return ((QueryParam) annotation).value();
            } else if (annotation instanceof PathParam) {
                return ((PathParam) annotation).value();
            }
        }
        throw new RuntimeException("Trying to create ParameterDescriptor for neither @QueryParam nor @PathParam");
    }

    private Class<? extends ParamFormatter> getParamFormatter(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof ParamFormat) {
                return ((ParamFormat) annotation).value();
            }
        }
        return null;
    }

    private ParamType getParamType(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof QueryParam) {
                return ParamType.QUERY;
            } else if (annotation instanceof PathParam) {
                return ParamType.PATH;
            }
        }
        throw new RuntimeException("Trying to create ParameterDescriptor for neither @QueryParam nor @PathParam");
    }

    private ParamMatchingStrategy getParamMatchingStrategy(Annotation[] annotations) {
        for (Annotation parameterAnnotation : annotations) {
            if (parameterAnnotation instanceof ParamMatchedBy) {
                return ((ParamMatchedBy) parameterAnnotation).value();
            }
        }
        return ParamMatchingStrategy.EQUAL_TO;
    }
}