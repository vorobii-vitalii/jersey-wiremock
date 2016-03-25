package jerseywiremock.annotations.factory;

import jerseywiremock.core.stub.BaseRequestMocker;
import jerseywiremock.core.verify.BaseRequestVerifyBuilder;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

class MockerTypeChecker {
    private final MockerMethodSelector methodSelector;

    MockerTypeChecker(MockerMethodSelector methodSelector) {
        this.methodSelector = methodSelector;
    }

    <T> void checkReturnTypes(Class<T> mockerType) {
        List<Method> methods = methodSelector.getMethodsForType(mockerType);

        List<Method> badMethods = selectBadMethods(methods);

        if (!badMethods.isEmpty()) {
            throwExceptionFor(badMethods);
        }
    }

    private List<Method> selectBadMethods(List<Method> methods) {
        List<Method> badMethods = new LinkedList<>();
        for (Method method : methods) {
            Class<?> returnType = method.getReturnType();
            if (!(isMocker(returnType) || isVerifier(returnType))) {
                badMethods.add(method);
            }
        }
        return badMethods;
    }

    private boolean isMocker(Class<?> returnType) {
        return BaseRequestMocker.class.isAssignableFrom(returnType);
    }

    private boolean isVerifier(Class<?> returnType) {
        return BaseRequestVerifyBuilder.class.isAssignableFrom(returnType);
    }

    private void throwExceptionFor(List<Method> badMethods) {
        StringBuilder builder = new StringBuilder();
        builder.append("All methods must return request mockers or verifiers. The following methods do not:\n");
        for (Method badMethod : badMethods) {
            builder.append("\t").append(badMethod).append("\n");
        }
        throw new RuntimeException(builder.toString());
    }
}
