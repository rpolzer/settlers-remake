package jsettlers.graphics.androidui.hud;

import jsettlers.graphics.androidui.R;
import jsettlers.graphics.androidui.menu.AndroidMenu;
import jsettlers.graphics.androidui.menu.AndroidMenuPutable;
import jsettlers.graphics.androidui.menu.BuildMenu;
import jsettlers.graphics.androidui.menu.GameMenu;
import jsettlers.graphics.map.ScreenPosition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HudFragment extends AndroidMenu {
	private ButtonForSelectionManager selectionSetter;

	public HudFragment() {
		super();
	}

	@Override
	public void setMenuPutable(AndroidMenuPutable putable) {
		super.setMenuPutable(putable);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.hud, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.findViewById(R.id.button_build).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPutable().showMenuFragment(new BuildMenu());
			}
		});

		view.findViewById(R.id.button_menu).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPutable().showMenuFragment(new GameMenu());
			}
		});

		View buttonSelection = view.findViewById(R.id.button_selection);
		selectionSetter = new ButtonForSelectionManager(getPutable(), (ImageButton) buttonSelection);
		getPutable().getChangeObserveable().addMapSelectionListener(selectionSetter);

		ScreenPosition screen = getPutable().getMapContext().getScreen();
		((NavigationView) view.findViewById(R.id.navigate)).setScreenPositionToInfluence(screen);

		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		getPutable().getChangeObserveable().removeMapSelectionListener(selectionSetter);
		super.onDestroyView();
	}
}
