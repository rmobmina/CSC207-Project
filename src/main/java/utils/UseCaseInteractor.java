package utils;

/**
 * The `UseCaseInteractor` class serves as a base class for use cases in the application.
 * It provides a mechanism to track whether a use case has failed.
 */
public abstract class UseCaseInteractor {

    /**
     * Indicates whether the use case has failed.
     */
    protected boolean useCaseFailed;

    /**
     * Checks if the use case has failed.
     *
     * @return `true` if the use case has failed; otherwise, `false`.
     */
    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
