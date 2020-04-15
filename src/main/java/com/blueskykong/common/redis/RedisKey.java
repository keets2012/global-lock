package com.blueskykong.common.redis;

/**
 * Redis key 清单<br>
 * Key 命令规则：以具体项目名或服务名打头，使用 dot 连接其他部分。<br>
 * 以 Redis 相关类型简写结尾，方便一眼识别每个 key 的类型<br>
 * Hash 类型的 fieldName 也可以定义在此处
 * @author keets
 */
public class RedisKey {
	/** key 不同部分的连接符 */
	public static final String DOT  = ".";

	/** Redis GEO 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String GEO  = "geo";

	/** Redis Hash 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String HASH = "hash";

	/** Redis HyperLogLog 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String LOG  = "log";

	/** Redis List 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String LIST = "list";

	/** Redis Set 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String SET  = "set";

	/** Redis String 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String STR  = "str";

	/** Redis SortedSet 类型简写，拼接到 key 的末尾，方便直接识别类型 */
	public static final String ZSET = "zset";


	/*-------------------- Notice-Service key list start --------------------*/
	/** 短信 & 通知服务名称 */
	public static final String NOTICE_SERVICE = "notice_service";

	/**
	 * 获取验证码 redis key
	 * @param mobile    手机号
	 * @param scene     短信发送场景
	 * @param requestId 阿里云短信发送 RequestId，短信唯一标识
	 * @return          验证码 redis key，格式如：notice_service.13333333333.superid_register.28F56CD0-66A0-4BFA-9829-9DC1E690D78C
	 */
	public static String getVerifyCodeKey(String mobile, String scene, String requestId) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(NOTICE_SERVICE).append(DOT).append(mobile).append(DOT).append(scene).append(DOT).append(requestId);
		return stringBuffer.toString();
	}
	/** 验证码 field */
	public static final String VERIFY_CODE_FIELD_CODE = "code";
	/** 验证码 field */
	public static final String VERIFY_CODE_FIELD_STATE = "state";
	/*-------------------- Notice-Service key list end --------------------*/


}
