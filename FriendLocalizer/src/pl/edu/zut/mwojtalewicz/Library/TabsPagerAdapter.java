package pl.edu.zut.mwojtalewicz.Library;

import pl.edu.zut.mwojtalewicz.friendlocalizerv2.MapFragment;
import pl.edu.zut.mwojtalewicz.friendlocalizerv2.ProfileFragment;
import pl.edu.zut.mwojtalewicz.friendlocalizerv2.UserFriendListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new ProfileFragment();
		case 1:
			return new UserFriendListFragment();
		case 2:
			return new MapFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}
}
