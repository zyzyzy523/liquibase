package liquibase.precondition;

public interface Conditional {
    public Preconditions getPreconditions();

    public void setPreconditions(Preconditions precond);

}
