package es.eucm.cytochallenge.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import es.eucm.cytochallenge.CytoChallenge;
import es.eucm.cytochallenge.model.PreviewConfig;
import es.eucm.cytochallenge.utils.ChallengeResourceProvider;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 552;
		config.overrideDensity = 160;

		double[][] pollygons = new double[][]{
				new double[]{
						899.98813,140.06917,
						910.10036,255.34855,
						960.66149,346.35858,
						999.08795,334.22391,
						1055.7164,348.38103,
						1114.3673,350.40347,
						1124.4796,273.55055,
						1092.1204,176.47318,
						1065.8286,81.418254,
						1009.2002,30.857123,
						930.32481,30.857123,
						891.89835,59.171357,
						893.9208,111.75493,
						906.05547,146.1365,
						899.98813,140.06917
				},
				new double[]{
						438.87062,245.23632,
						477.29708,277.59544,
						527.85821,293.775,
						568.30711,271.52811,
						562.23978,206.80986,
						523.81332,166.36095,
						477.29708,164.33851,
						442.91551,182.54052,
						430.78084,218.94453,
						438.87062,245.23632
				},
				new double[]{
						402.4666,627.47847,
						501.56642,613.32135,
						519.76843,550.62555,
						505.61131,487.92975,
						461.11752,459.61551,
						406.51149,475.79508,
						335.72591,520.28887,
						339.7708,601.18668,
						376.17482,641.63559,
						402.4666,627.47847
				},
				new double[]{
						665.38449,1015.788,
						628.98047,1070.394,
						647.18248,1108.8204,
						699.76605,1112.8653,
						715.94562,1141.1796,
						758.41697,1163.4265,
						800.88832,1135.1122,
						815.04543,1098.7082,
						825.15766,1068.3715,
						772.57408,1040.0573,
						715.94562,1011.7431,
						665.38449,1015.788
				}
		};

		for(int j = 0; j < pollygons.length; j++) {
			double[] points = pollygons[j];

			System.out.println("[");

			for (int i = 0; i < points.length; i += 2) {
				double x = points[i];

				double y = points[i + 1];

				double y1 = 1125 - y;

				System.out.print("" + x + ", " + y1);
				System.out.println(i + 2 == points.length ? "" : ",");
			}
			System.out.print("]");
			System.out.println(j == pollygons.length ? "" : ",");
		}


		boolean test = false;
		PreviewChallengeResourceProvider challengeResourceProvider = null;
		if(test) {
			challengeResourceProvider = new PreviewChallengeResourceProvider();
			PreviewConfig prevConfig = new PreviewConfig();
			prevConfig.setImagesHost("http://localhost:3000/uploads/");
			prevConfig.setChallengeHost("http://localhost:3000/challenges/");
			prevConfig.setChallengeId("57e17c8814c57aa416a989a1");
			challengeResourceProvider.setPreviewConfig(prevConfig);
		}

		new LwjglApplication(new CytoChallenge(challengeResourceProvider, new DesktopResolver()), config);
	}
}
