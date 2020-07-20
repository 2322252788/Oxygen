package me.Oxygen.utils.other;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;

public class MathUtil {
	public static Random random = new Random();

	public static double toDecimalLength(double in, int places) {
		return Double.parseDouble(String.format("%." + places + "f", in));
	}

	public static double round(double in, int places) {
		places = (int) MathHelper.clamp_double(places, 0.0, 2.147483647E9);
		return Double.parseDouble(String.format("%." + places + "f", in));
	}

	public static boolean parsable(String s, byte type) {
		try {
			switch (type) {
			case 0: {
				Short.parseShort(s);
				break;
			}
			case 1: {
				Byte.parseByte(s);
				break;
			}
			case 2: {
				Integer.parseInt(s);
				break;
			}
			case 3: {
				Float.parseFloat(s);
				break;
			}
			case 4: {
				Double.parseDouble(s);
				break;
			}
			case 5: {
				Long.parseLong(s);
			}
			default: {
				break;
			}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static double getIncremental(double val, double inc) {
		double one = 1 / inc;
		return Math.round(val * one) / one;
	}

	public static double square(double in) {
		return in * in;
	}

	public static double randomDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	public static double randomFloat(float min, float max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}

	public static double getBaseMovementSpeed() {
		double baseSpeed = 0.2873;
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
			baseSpeed *= 1.0
					+ 0.2 * (double) (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
		}
		return baseSpeed;
	}

	public static double getHighestOffset(double max) {
		double i = 0.0;
		while (i < max) {
			int[] arrn = new int[5];
			arrn[0] = -2;
			arrn[1] = -1;
			arrn[3] = 1;
			arrn[4] = 2;
			int[] arrn2 = arrn;
			int n = arrn.length;
			int n2 = 0;
			while (n2 < n) {
				int offset = arrn2[n2];
				if (Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer,
						Minecraft.getMinecraft().thePlayer.getEntityBoundingBox().offset(Minecraft.getMinecraft().thePlayer.motionX * (double) offset,
								i, Minecraft.getMinecraft().thePlayer.motionZ * (double) offset))
						.size() > 0) {
					return i - 0.01;
				}
				++n2;
			}
			i += 0.01;
		}
		return max;
	}

	public static class NumberType {
		public static final byte SHORT = 0;
		public static final byte BYTE = 1;
		public static final byte INT = 2;
		public static final byte FLOAT = 3;
		public static final byte DOUBLE = 4;
		public static final byte LONG = 5;

		public static byte getByType(Class cls) {
			if (cls == Short.class) {
				return 0;
			}
			if (cls == Byte.class) {
				return 1;
			}
			if (cls == Integer.class) {
				return 2;
			}
			if (cls == Float.class) {
				return 3;
			}
			if (cls == Double.class) {
				return 4;
			}
			if (cls == Long.class) {
				return 5;
			}
			return -1;
		}
	}

	public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

	public static int randomNumber(int max, int min) {
      return -min + (int)(Math.random() * (double)(max - -min + 1));
   }

	public static double randomNumber(double max, double min) {
		return Math.random() * (max - min) + min;
	}

	public static float getRandom() {
        return random.nextFloat();
    }

	public static int getRandom(int cap) {
        return random.nextInt(cap);
    }

	public static double getRandomInRange(double min, double max) {
		Random random = new Random();
		double range = max - min;
		double scaled = random.nextDouble() * range;
		if (scaled > max) {
			scaled = max;
		}
		double shifted = scaled + min;

		if (shifted > max) {
			shifted = max;
		}
		return shifted;
	}

	public static float getRandomInRange(float min, float max) {
		Random random = new Random();
		float range = max - min;
		float scaled = random.nextFloat() * range;
		float shifted = scaled + min;
		return shifted;
	}

	public static int getRandomInRange(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
