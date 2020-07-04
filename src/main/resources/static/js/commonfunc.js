/**各种格式验证**/
var fmReg = {
    // enName: /^([A-Za-z]{0,}\s{1}[A-Za-z]{0,})(\s{1}[A-Za-z]{0,})?/,//英文名称 最大长度为19位，超过19位取姓的全拼，名的首字母；需要大写
    enName: /^[A-Za-z]{0,}$/, //英文名称 最大长度为19位，超过19位取姓的全拼，名的首字母；需要大写
    mobile: /^1[0-9]{10}$/, //手机
    zipCode: /^\d{6}$/, //邮编 6位数字
    email: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/, //邮箱xx@xx.xxx
    tel: /^0[0-9]{2,3}-[1-9]\d{6,7}$/, //固定电话 区号以0开头的3或4位数字 电话非0开通的7或8位数字
    cerNo: /(^[1-9]\d{16}[X|x]$)|(^[1-9]\d{17}$)|(^[1-9]\d{14}$)/, //身份证号码15位非0开头纯数字或18位非0开头纯数字/非0开头+数字+X
    qitaCerNo: /^[0-9a-zA-Z]{1,20}$/, //证件号码只支持大小写字母和数字,且不多于20个！
    managerId: /^\d{8}$|^\d{10}$/, //客户经理号8或10
    managerId12: /^\d{12}$/, //客户经理号12
    pwD6: /^\d{6}$/, //密码\6位数字
    num: /^(([0-9]|([1-9][0-9]{0,9}))((\.[0-9]{1,2})?))$/, //金额
    telQh: /^0[0-9]{2,3}$/, //固定电话 区号以0开头的3或4位数字
    telHm: /^[1-9]\d{6,7}$/, //固定电话  电话非0开通的7或8位数字
    numSZ: /^[1-9]\d*$/, //数字
    eleNum: /^(([0-9]|([1-9][0-9]{0,5}))((\.[0-9]{1,2})?))$/, //电子签约金额
    loanNum: /^(([0-9]|([1-9][0-9]{0,10}))((\.[0-9]{1,2})?))$/, //贷款金额
    address: /^[a-zA-Z0-9\u4e00-\u9fa5\s#\-]*$/, //只能输入汉字字母空格
    cnName: /^[a-zA-Z\u4e00-\u9fa5]+$/, //只能输入汉字字母
    money: /^((\d{1,3}(,\d{3})*)|(\d+))(\.\d{1,2})?$/, //固定格式的金额 11，123.00
    pwd8: /^([a-z]|[A-Z]|[0-9]){6,10}$/,
    date: /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/, //date：yyyy-mm-dd
    time: /^(?:[01]\d|2[0-3])(?::[0-5]\d){2}$/, //时间 hh:ss:mm
    numZU: /^[0-9]+$/,
    loanTel: /^[0-9][0-9\-]{1,20}$/,
    organizationCode: /^[0-9A-Z]{0,19}$/, //组织机构代码支持大写字母和数字,且不多于19个！
    corCreditNO: /^[0-9A-Z]{18}$/,//机构信用代码证号仅可输入18个大写字母和数字
    taxFileNo: /^[0-9A-Z]{0,20}$/,//国税登记号 不多于20个
    localTaxFileNo: /^[0-9A-Z]{0,20}$/,//地税登记号 不多于20个
    approvalId:/^[0-9A-Z]{0,14}$/,//开户许可证号，大写字母+数字，不超过14
    cititel: /^0[0-9]{2,3}[-]{0,1}[1-9]\d{6,7}$/, //固定电话不带- 区号以0开头的3或4位数字 电话非0开通的7或8位数字
    income: /^[1-9]*[1-9][0-9]*$/
};