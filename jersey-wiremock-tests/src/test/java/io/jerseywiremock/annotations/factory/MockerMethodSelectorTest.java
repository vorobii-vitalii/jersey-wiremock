package io.jerseywiremock.annotations.factory;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.jerseywiremock.core.stub.request.GetSingleRequestStubber;
import io.jerseywiremock.core.verify.GetRequestVerifier;

public class MockerMethodSelectorTest {
    @Test
    public void allMethodsFromInterfaceAreSelected() {
        // given
        MockerMethodSelector selector = new MockerMethodSelector();

        // when
        List<Method> methods = selector.getMethodsForType(TestInterface.class);

        // then
        assertThat(methods).extracting("name").containsOnly("getRequestStubber", "getRequestVerifier");
    }

    @Test
    public void allAbstractMethodsFromAbstractClassAreSelected() {
        // given
        MockerMethodSelector selector = new MockerMethodSelector();

        // when
        List<Method> methods = selector.getMethodsForType(TestClass.class);

        // then
        assertThat(methods).extracting("name").containsOnly("getRequestStubber");
    }

    @SuppressWarnings("unused")
    private interface TestInterface {
        GetSingleRequestStubber<Integer> getRequestStubber();
        GetRequestVerifier getRequestVerifier();
    }

    @SuppressWarnings("unused")
    private static abstract class TestClass {
        abstract GetSingleRequestStubber<Integer> getRequestStubber();

        GetRequestVerifier getRequestVerifier() {
            return null;
        }
    }
}