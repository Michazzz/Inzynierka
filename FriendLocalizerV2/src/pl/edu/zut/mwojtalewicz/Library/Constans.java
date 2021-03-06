package pl.edu.zut.mwojtalewicz.Library;

public class Constans {
	
	public static final String Tag = "Michazzz";
	
	public static final String loginURL = "http://www.dyplomowa.mwojtalewicz.cba.pl/mobile_log_in.php";
	public static final String registerURL = "http://www.dyplomowa.mwojtalewicz.cba.pl/mobile_log_in.php";
	
	public static final String login_tag = "login";
	public static final String register_tag = "register";
	public static final String searchFriends_tag = "searchfriends";
	public static final String inviteFriend_tag = "invite";
	public static final String refreshFriendsList = "refreshfriends";
	public static final String acceptInvite = "accept";
	public static final String declineInvite = "decline";
	public static final String userFriendList = "userfriendlist";
	public static final String removeUserFromFriends = "removefriend";
	public static final String userGpsPosition = "usergpsposition";
	public static final String friendsLocation = "locatefriends";
	public static final String inviteReplay = "acceptreplay";
	public static final String checkInvitations = "checkinvitations";
	
	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "android_api";
	public static final String TABLE_LOGIN = "login";
	public static final String TABLE_FRIENDS = "friends";
	public static final String KEY_ID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_UID_INVITING = "uid_inviting";
	public static final String KEY_UID_INVITED = "uid_invited";
	public static final String KEY_STATUS = "status";
	public static final String KEY_LASTNAME = "lastname";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_UID = "uid";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_SUCCESS = "success";
	public static final String KEY_ERROR = "error";
	public static final String KEY_ERROR_MSG = "error_msg";
	public static final String KEY_LEVEL = "level";
	
	public static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," 
			+ KEY_NAME + " TEXT,"
			+ KEY_LASTNAME + " TEXT,"
			+ KEY_EMAIL + " TEXT UNIQUE,"
			+ KEY_UID + " TEXT,"
			+ KEY_CREATED_AT + " TEXT" + ")";
}
