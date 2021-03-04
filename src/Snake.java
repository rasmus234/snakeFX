import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum Direction {
    UP,
    DOWN,
    RIGHT,
    LEFT
}

public class Snake {
    private List<BodyPart> bodyParts = new ArrayList<>();

    public Snake(BodyPart... bodyParts) {
        this.bodyParts.addAll(Arrays.asList(bodyParts));
    }

    public Snake(List<BodyPart> bodyParts) {
        this.bodyParts.addAll(bodyParts);
    }

    public Snake(Snake snake) {
        this(snake.getBodyParts());
    }

    public BodyPart getHead() {
        return this.bodyParts.get(0);
    }

    public void setHead(BodyPart bodyPart) {

        this.bodyParts.add(0, bodyPart);
    }

    public BodyPart getTail() {
        return this.bodyParts.get(bodyParts.size() - 1);
    }

    public void setTail(BodyPart bodyPart) {
        this.bodyParts.set(this.bodyParts.size() - 1, bodyPart);
    }

    public void removeTail() {
        this.bodyParts.remove(bodyParts.size() - 1);
    }

    public List<BodyPart> getBodyParts() {
        return bodyParts;
    }


    @Override
    public String toString() {
        return "Snake{" +
                "bodyParts=" + bodyParts +
                ", currentDirection=" +
                '}';
    }
}
