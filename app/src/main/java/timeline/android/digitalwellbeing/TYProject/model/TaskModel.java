package timeline.android.digitalwellbeing.TYProject.model;

public class TaskModel {
    private String taskName;
    private String status;
    private int id;

    public TaskModel(String taskName, String status) {
        this.taskName = taskName;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
