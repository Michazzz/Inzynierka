package pl.edu.zut.mwojtalewicz.friendLocalizerLibrary;

public class Constans {
	
	
	public static String loginURL = "http://www.dyplomowa.mwojtalewicz.cba.pl/mobile_log_in.php";
	public static String registerURL = "http://www.dyplomowa.mwojtalewicz.cba.pl/mobile_log_in.php";
	
	public static String login_tag = "login";
	public static String register_tag = "register";
	public static String searchFriends_tag = "searchfriends";
	public static String inviteFriend_tag = "invite";
	public static String refreshFriendsList = "refreshfriends";
	public static String acceptInvite = "accept";
	public static String declineInvite = "decline";
	public static String userFriendList = "userfriendlist";
	public static String removeUserFromFriends = "removefriend";
	
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
	public static String KEY_SUCCESS = "success";
	public static String KEY_ERROR = "error";
	public static String KEY_ERROR_MSG = "error_msg";
	
	public static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," 
			+ KEY_NAME + " TEXT,"
			+ KEY_LASTNAME + " TEXT,"
			+ KEY_EMAIL + " TEXT UNIQUE,"
			+ KEY_UID + " TEXT,"
			+ KEY_CREATED_AT + " TEXT" + ")";
	
	public static final String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + "("
			+ KEY_ID + " INTEGER PRIMARY KEY," 
			+ KEY_UID_INVITING + " INTEGER,"
			+ KEY_UID_INVITED + " INTEGER,"
			+ KEY_STATUS + " INTEGER,"
			+ KEY_CREATED_AT + " TEXT" + ")";
}
