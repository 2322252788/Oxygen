package cn.rainbow.oxygen.utils.other;

import net.minecraft.util.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {

	public static double roundToPlace(double value, int places) {
		try {
			if (places < 0) {
				return value;
			} else {
				BigDecimal bd = new BigDecimal(value);
				bd = bd.setScale(places, RoundingMode.HALF_UP);
				return bd.doubleValue();
			}
		} catch(Exception e) {
			return 0;
		}
	}

	public static double round(double in, int places) {
		places = (int) MathHelper.clamp_double(places, 0.0D, 2.147483647E9D);
		return Double.parseDouble(String.format("%." + places + "f", in));
	}

	public static double getIncremental(double val, double inc) {
		double one = 1.0D / inc;
		return (double) Math.round(val * one) / one;
	}

	public static boolean isInteger(Double variable) {
		return variable == Math.floor(variable) && !Double.isInfinite(variable);
	}

	public static double randomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

}
