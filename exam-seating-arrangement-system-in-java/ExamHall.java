import java.util.ArrayList;
import java.util.List;

public class ExamHall {
    private int hallId;
    private int seatingCapacity;
    private List<Student> allocatedStudents;

    public ExamHall(int hallId, int seatingCapacity) {
        this.hallId = hallId;
        this.seatingCapacity = seatingCapacity;
        this.allocatedStudents = new ArrayList<>();
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public List<Student> getAllocatedStudents() {
        return allocatedStudents;
    }

    public void setAllocatedStudents(List<Student> allocatedStudents) {
        this.allocatedStudents = allocatedStudents;
    }
}
