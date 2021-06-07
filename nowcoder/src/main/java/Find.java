/**
 * 在一个二维数组中（每个一维数组的长度相同），每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
 * 请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 * [
 * [1,2,8,9],
 * [2,4,9,12],
 * [4,7,10,13],
 * [6,8,11,15]
 * ]
 * 给定 target = 7，返回 true。
 * <p>
 * 给定 target = 3，返回 false。
 *
 * @author keminfeng
 * @date 2021-06-07 23:00
 */
public class Find {

    public static void main(String[] args) {
//        int[][] arr = {
//                {1, 2, 8, 9},
//                {2, 4, 9, 12},
//                {4, 7, 10, 13},
//                {6, 8, 11, 15}};
        int[][] arr = {{}};
        new Find().find(11, arr);
    }

    public boolean find(int target, int[][] array) {
        int weight = array[0].length;
        if (weight == 0) {
            return false;
        }
        for (int[] lineArray : array) {
            if (lineArray[weight - 1] >= target) {
                int temp = weight;
                while (temp > 0) {
                    if (lineArray[temp - 1] == target) {
                        return true;
                    } else if (lineArray[weight - 1] > target) {
                        temp--;
                    } else {
                        break;
                    }
                }
            } else if (lineArray[0] > target) {
                return false;
            }
        }
        return false;
    }
}
