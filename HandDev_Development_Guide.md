#HandDev 공식 표준 문서
##변수 네이밍 규칙
###일반변수
--> Java의 정석 Camel Case로 작성한다.

###안드로이드 Activity/Context/Fragment
--> mActivity / mContext / mFragment 의 형식으로 작성한다.

    Activity mActivity;

###안드로이드 버튼/텍스트 뷰 등등
--> mLoginButton == m+역할+객체이름 으로 작성한다.

    Button mLoginButton;

###레이아웃 리소스 버튼/텍스트뷰 등
--> 역할_button / 역할_textview 으로 작성한다.

--> 약자는 사용하지 않는다.

--> 중복일 경우, button0X로 순서대로 네이밍한다.

--> "역할 + '_' + 각 개체 이름 소문자 + 숫자" 의 형식으로 작성한다.

    android:name = "@+id/button01";

##패키지 관리
--> Activity/Fragment/etc 등 모두 나누어 관리한다.

--> Activity의 레이아웃명은 activity_OOO 로 작성한다.

--> Fragment의 레이아웃명은 fragment_OOO 로 작성한다.

activity_OOO.xml / fragment_OOO.xml

##Git Commit 관리
--> 1가지 기능 개발시 바로 커밋한다.

--> 커밋은 "액티비티명_메소드명_(ADD/REMOVE/EDIT)"로 작성한다.

--> 개발 된 메소드 위에 주석으로 역할을 영어로 작성한다.

--> Git Issue 기능 활용한다.

--> README.md 파일에는 프로젝트 목적과 사용된 Library의 현황을 작성한다.


