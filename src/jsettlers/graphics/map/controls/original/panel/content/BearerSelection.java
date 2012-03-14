package jsettlers.graphics.map.controls.original.panel.content;

import jsettlers.common.images.EImageLinkType;
import jsettlers.common.images.ImageLink;
import jsettlers.common.movable.EMovableType;
import jsettlers.common.selectable.ISelectionSet;
import jsettlers.graphics.action.ConvertAction;
import jsettlers.graphics.localization.Labels;
import jsettlers.graphics.map.controls.original.panel.IContextListener;
import jsettlers.graphics.utils.UIPanel;

public class BearerSelection implements IContentProvider {
	private UIPanel panel;
	private int count;

	public BearerSelection(ISelectionSet selection) {
		panel = new UIPanel();
		count = selection.getMovableCount(EMovableType.BEARER);

		addPioneers(.7f);
		addGeologists(.45f);
		addThieves(.2f);
	}

	private void addPioneers(float bottom) {
		ImageLink imageLink = new ImageLink(EImageLinkType.GUI, 14, 210, 0);

		drawButtongroup(bottom, imageLink, EMovableType.PIONEER);
	}

	private void addThieves(float bottom) {
		ImageLink imageLink = new ImageLink(EImageLinkType.GUI, 14, 189, 0);

		drawButtongroup(bottom, imageLink, EMovableType.THIEF);
	}

	private void addGeologists(float bottom) {
		ImageLink imageLink = new ImageLink(EImageLinkType.GUI, 14, 192, 0);

		drawButtongroup(bottom, imageLink, EMovableType.GEOLOGIST);
	}

	private void drawButtongroup(float bottom, ImageLink imageLink,
	        EMovableType type) {
		UIPanel icon = new UIPanel();
		icon.setBackground(imageLink);

		UILabeledButton convert1 =
		        new UILabeledButton(Labels.getString("convert_1_to_" + type),
		                new ConvertAction(type, (short) 1));
		UILabeledButton convertall =
		        new UILabeledButton(Labels.getString("convert_all_to_" + type),
		                new ConvertAction(type, Short.MAX_VALUE));

		panel.addChild(icon, .1f, bottom, .3f, bottom + .2f);
		panel.addChild(convert1, .3f, bottom + .1f, .9f, bottom + .2f);
		panel.addChild(convertall, .3f, bottom, .9f, bottom + .1f);
	}

	@Override
	public UIPanel getPanel() {
		return panel;
	}

	@Override
	public IContextListener getContextListener() {
		return null;
	}

	@Override
	public ESecondaryTabType getTabs() {
		return null;
	}

}
