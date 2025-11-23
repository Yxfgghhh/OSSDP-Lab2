import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Solution类的单元测试。
 * 
 * 测试用例设计总体原则：
 * 
 * 1. 等价类划分原则：
 *    将输入划分为若干个等价类，从每个等价类中选取代表性的数据作为测试用例。
 *    - 有效等价类：符合版本号格式规范的输入，如 "1.0", "2.5.33"。
 *    - 无效等价类：不符合格式规范的输入（本题未要求处理此类情况，故暂不考虑）。
 * 
 * 2. 边界值分析原则：
 *    关注输入变量的边界值，这些地方往往容易出错。
 *    - 版本号组成部分为0：如 "0.1", "1.0"。
 *    - 版本号组成部分有前导零：如 "1.01", "001.000"。
 *    - 一个版本号是另一个的前缀：如 "1.2" vs "1.2.3"。
 * 
 * 3. 场景覆盖原则：
 *    覆盖题目描述中提到的各种典型场景。
 *    - 版本号长度相同，部分修订号不同。
 *    - 版本号长度不同，但较短的版本号是较长版本号的前缀且前面部分都相同。
 *    - 版本号包含前导零。
 * 
 * 4. 错误推测原则：
 *    根据经验和直觉推测程序可能出现错误的地方。
 *    - 其中一个版本号只有一个修订号。
 *    - 修订号非常长（虽然本题用Integer可以处理，但测试其逻辑正确性）。
 */
public class SolutionTest {

    private final Solution solution = new Solution();

    /**
     * 测试目的：验证当两个版本号长度相同，且所有对应修订号都相等（包括前导零情况）时，比较结果为0。
     * 测试用例：
     * - version1 = "1.01", version2 = "1.001"
     *   预期结果：0
     *   （解释："01" 和 "001" 经转换后都是整数 1）
     */
    @Test
    public void testEqualVersionsWithLeadingZeros() {
        String version1 = "1.01";
        String version2 = "1.001";
        assertEquals(0, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证当一个版本号是另一个版本号的前缀，且前面所有修订号都相等时，较短的版本号更小。
     * 测试用例：
     * - version1 = "1.0", version2 = "1.0.0"
     *   预期结果：0
     *   （解释：version1 缺少的修订号视为 0，因此 "1.0.0" 和 "1.0.0" 相等）
     */
    @Test
    public void testVersionsWithDifferentLengths_PrefixEqual() {
        String version1 = "1.0";
        String version2 = "1.0.0";
        assertEquals(0, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证当两个版本号在较早的修订号出现差异时，比较结果能正确反映大小。
     * 测试用例：
     * - version1 = "0.1", version2 = "1.1"
     *   预期结果：-1
     *   （解释：第一个修订号 0 < 1，因此 version1 < version2）
     */
    @Test
    public void testVersionsDifferAtEarlyRevision() {
        String version1 = "0.1";
        String version2 = "1.1";
        assertEquals(-1, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证当两个版本号长度不同，且较长版本号在超出较短版本号的部分有非零修订号时，较长的版本号更大。
     * 测试用例：
     * - version1 = "1.2.3.4", version2 = "1.2.3"
     *   预期结果：1
     *   （解释：version2 的第四个修订号视为 0，而 version1 的第四个修订号是 4，因此 version1 > version2）
     */
    @Test
    public void testLongerVersionIsGreater() {
        String version1 = "1.2.3.4";
        String version2 = "1.2.3";
        assertEquals(1, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证当两个版本号长度不同，且较短版本号是较长版本号的前缀但后面有更小的修订号时，较短的版本号更大。
     * 测试用例：
     * - version1 = "1.3", version2 = "1.2.99"
     *   预期结果：1
     *   （解释：第二个修订号 3 > 2，因此 version1 > version2，后续不再比较）
     */
    @Test
    public void testShorterVersionIsGreater() {
        String version1 = "1.3";
        String version2 = "1.2.99";
        assertEquals(1, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证当版本号只有一个修订号时，比较逻辑依然正确。
     * 测试用例：
     * - version1 = "123", version2 = "45"
     *   预期结果：1
     *   （解释：直接比较整数 123 > 45）
     */
    @Test
    public void testSingleRevisionVersions() {
        String version1 = "123";
        String version2 = "45";
        assertEquals(1, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证版本号中包含多个零的修订号时，比较逻辑正确。
     * 测试用例：
     * - version1 = "0.0.0", version2 = "0"
     *   预期结果：0
     *   （解释：所有修订号都为0，视为相等）
     */
    @Test
    public void testVersionsWithAllZeros() {
        String version1 = "0.0.0";
        String version2 = "0";
        assertEquals(0, solution.compareVersion(version1, version2));
    }

    /**
     * 测试目的：验证在较晚的修订号出现差异时，比较结果能正确反映大小。
     * 测试用例：
     * - version1 = "2.5.33.999", version2 = "2.5.33.1000"
     *   预期结果：-1
     *   （解释：前三个修订号都相同，第四个修订号 999 < 1000）
     */
    @Test
    public void testVersionsDifferAtLateRevision() {
        String version1 = "2.5.33.999";
        String version2 = "2.5.33.1000";
        assertEquals(-1, solution.compareVersion(version1, version2));
    }
}