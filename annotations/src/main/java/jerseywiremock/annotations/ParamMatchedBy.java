package jerseywiremock.annotations;

import jerseywiremock.core.ParamMatchingStrategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParamMatchedBy {
    ParamMatchingStrategy value();
}
