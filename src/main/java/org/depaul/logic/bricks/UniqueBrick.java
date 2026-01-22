package org.depaul.logic.bricks;
import org.depaul.logic.util.Operations;

import java.util.ArrayList;
import java.util.List;

public final class UniqueBrick implements Brick{

    private final List<int[][]> brickMatrixList = new ArrayList<>();

    public UniqueBrick() {
        brickMatrixList.add(new int[][]{
                {0, 0, 8, 8},
                {0, 8, 8, 0},
                {0, 0, 8, 0},
                {0, 0, 0, 0}
        });
        brickMatrixList.add(new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 8},
                {0, 8, 8, 8},
                {0, 0, 8, 0}
        });
        brickMatrixList.add(new int[][]{
                {0, 0, 0, 0},
                {0, 8, 0, 0},
                {0, 8, 8, 0},
                {8, 8, 0, 0}
        });
        brickMatrixList.add(new int[][]{
                {0, 8, 0, 0},
                {8, 8, 8, 0},
                {8, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }
    @Override
    public List<int[][]> getBrickMatrixList() {
        return Operations.deepCopyList(brickMatrixList);
    }
}
