package jerseywiremock.core;

public class QueryParamMatchDescriptor {
    private final String paramName;
    private final String value;
    private final ParamMatchingStrategy matchingStrategy;

    public QueryParamMatchDescriptor(
            String paramName,
            String value,
            ParamMatchingStrategy matchingStrategy
    ) {
        this.paramName = paramName;
        this.value = value;
        this.matchingStrategy = matchingStrategy;
    }

    public String getParamName() {
        return paramName;
    }

    public String getValue() {
        return value;
    }

    public ParamMatchingStrategy getMatchingStrategy() {
        return matchingStrategy;
    }
}