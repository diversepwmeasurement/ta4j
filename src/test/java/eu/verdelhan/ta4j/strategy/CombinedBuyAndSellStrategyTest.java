package eu.verdelhan.ta4j.strategy;

import eu.verdelhan.ta4j.Indicator;
import eu.verdelhan.ta4j.Operation;
import eu.verdelhan.ta4j.OperationType;
import eu.verdelhan.ta4j.Strategy;
import eu.verdelhan.ta4j.mocks.MockIndicator;
import eu.verdelhan.ta4j.mocks.MockStrategy;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CombinedBuyAndSellStrategyTest {

	private Operation[] enter;

	private Operation[] exit;

	private MockStrategy buyStrategy;

	private MockStrategy sellStrategy;

	private CombinedBuyAndSellStrategy combined;

	@Test
	public void testeShoudEnter() {

		enter = new Operation[] { new Operation(0, OperationType.BUY), null, new Operation(2, OperationType.BUY), null,
				new Operation(4, OperationType.BUY) };
		exit = new Operation[] { null, null, null, null, null };

		buyStrategy = new MockStrategy(enter, null);
		sellStrategy = new MockStrategy(null, exit);

		combined = new CombinedBuyAndSellStrategy(buyStrategy, sellStrategy);

		assertThat(combined.shouldEnter(0)).isTrue();
		assertThat(combined.shouldEnter(1)).isFalse();
		assertThat(combined.shouldEnter(2)).isTrue();
		assertThat(combined.shouldEnter(3)).isFalse();
		assertThat(combined.shouldEnter(4)).isTrue();

		assertThat(combined.shouldExit(0)).isFalse();
		assertThat(combined.shouldExit(1)).isFalse();
		assertThat(combined.shouldExit(2)).isFalse();
		assertThat(combined.shouldExit(3)).isFalse();
		assertThat(combined.shouldExit(4)).isFalse();

	}

	@Test
	public void testeShoudExit() {

		exit = new Operation[] { new Operation(0, OperationType.SELL), null, new Operation(2, OperationType.SELL),
				null, new Operation(4, OperationType.SELL) };

		enter = new Operation[] { null, null, null, null, null };

		buyStrategy = new MockStrategy(enter, null);
		sellStrategy = new MockStrategy(null, exit);

		combined = new CombinedBuyAndSellStrategy(buyStrategy, sellStrategy);

		assertThat(combined.shouldExit(0)).isTrue();
		assertThat(combined.shouldExit(1)).isFalse();
		assertThat(combined.shouldExit(2)).isTrue();
		assertThat(combined.shouldExit(3)).isFalse();
		assertThat(combined.shouldExit(4)).isTrue();

		assertThat(combined.shouldEnter(0)).isFalse();
		assertThat(combined.shouldEnter(1)).isFalse();
		assertThat(combined.shouldEnter(2)).isFalse();
		assertThat(combined.shouldEnter(3)).isFalse();
		assertThat(combined.shouldEnter(4)).isFalse();
	}

	@Test
	public void testWhenBuyStrategyAndSellStrategyAreEquals() {
		Indicator<Double> first = new MockIndicator<Double>(new Double[] { 4d, 7d, 9d, 6d, 3d, 2d });
		Indicator<Double> second = new MockIndicator<Double>(new Double[] { 3d, 6d, 10d, 8d, 2d, 1d });

		Strategy crossed = new IndicatorCrossedIndicatorStrategy(first, second);

		combined = new CombinedBuyAndSellStrategy(crossed, crossed);

		for (int index = 0; index < 6; index++) {
			assertEquals(crossed.shouldEnter(index), combined.shouldEnter(index));
			assertEquals(crossed.shouldExit(index), combined.shouldExit(index));
		}
	}
}