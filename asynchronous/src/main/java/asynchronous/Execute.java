package asynchronous;

import java.util.Objects;

@FunctionalInterface
public interface Execute {
	
	void execute();

    default Execute andThen(Execute after) {
        Objects.requireNonNull(after);
        return () -> { execute(); after.execute(); };
    }
}
