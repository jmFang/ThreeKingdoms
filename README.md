# ThreeKingdoms
![](https://img.shields.io/badge/download-1K-brightgreen.svg)   ![](https://img.shields.io/badge/android-app-brightgreent.svg)


Android手机平台应用开发课程期中项目作业

# Project Design 

### 应用的主界面由三个Fragment组成，同时含有抽屉导航。

三个fragment分别是 三国志， 群英会，风云录

### 三国志 

展示一些群英会的战斗记录，战斗记录可来自不用用户，不需要提供注册登录，只要下载安装了本App，并且能够联网就能使用。用户只要``刷新``“三国志”的界面就能看到最新的一些对决记录。``刷新``功能作为拓展部分。



### 群英会

选择本地英雄进行对决，首先用户需要绑定自己的英雌，开始与系统的英雄对决，系统出战的英雄由随机产生。``用户每次对决结束的对决记录都会被发送到云端存``,前20次的记录可以本地缓存，而在``三国志``的界面就可以看到用户对决的记录。

英雄的属性：

```
    private int force;  //武力值(0~100)
    private int intelligence;  //智力值(0~100)
    private int leadership;   //统率值(0~100)
    private int army;   //军队数量:有统率值决定
    private int forage; // 粮草数量(0~1000)
```

游戏逻辑：

```
    //计算绝对战斗力，比较大小
    public int getEffectiveness() {
        int attr = (int)(force * 0.3 + intelligence * 0.3 + leadership * 0.4);
        int tarmy = army;
        if( tarmy > forage * 100) {
            double ratio = ((double) forage * 100) / army;
            tarmy = (int) (tarmy * ratio);
        }T
        return attr * 1000 + tarmy;
    }

```

### 风云录

卡片式布局，点击卡片会跳转到该任务的相信信息界面。卡片有简单的图片加英雄名字构成，上面是图片，下面是名字。只展示20个英雄，其他需要手动输入搜索，这20个英雄的图片可以是``Recource``里有的图片，即res目录下的资源。``【有时间可以做成“分页”式的，这样就可以缓解搜索的压力】``

对于搜索，先搜索本地数据库，如果本地数据库搜索不到，再搜索云端数据库，如果云端数据库也没有，则可以``AlertDialog``等形式返回‘暂时没有此人的信息’，提示，同时附带按钮“手动添加”，点击则跳转到‘添加英雄’的界面。

如果搜索在云端成功，则将该人物的资料下载并保存到本地数据库，然后呈现给用户，过程可能有点慢，可考虑做一个进度条，提高用户体验，而不是完全没有动作响应。

需要``增加搜索框，用于搜索``。因为如果全部本地的缓存都显示，则列表太长，不便于用户体验。


### 人物详情界面

卡片式布局，MD动画效果，已经做好。


### 抽屉导航

``添加英雄``：点击添加英雄，跳转到英雄添加的界面，上传图片，填写资料，点击提交，人物信息被``提交到云端服务器``，提交成功会出现“添加成功”的提示。这部分已经完成。

添加完成之后，需要与``HeroListFragment``进行通信，注意，这里是``活动与碎片之间的通信``（活动与活动之间的通信方法未必可用，自己尝试）,将添加的英雄同步到``风云录``,同时``写入本地数据库``作为缓存。

``我的英雄``：设定用户自己的英雄，用于对决时使用。

``删除英雄``：删除的是本地数据库里的英雄，不会删除云端的英雄。。如果本地数据库没有这个英雄，这删除失败。

``邀请好友``：这是App的``扩展``功能(暂时不做)，暂定使用社交App的分享SDK，如Mob后台服务(多种社交APP的分享功能)、微信开放平台（支持微信朋友圈和好友分享）

``设置``：``背景音乐``的设定，还有什么其他的设置，自己open mind

### 关于云端和本地缓存

目前有两个映射对象模型已使用，分别是``Hero``和``LocalHero``,``Hero``用于云端，``LocalHero``用于本地，之所以不能共用一个，是因为对于云端来说，上传图片，该图片视为文件,数据类型为``File``，这在本地无法直接使用，甚至转换成功``Bitmap``也很有点麻烦，所以干脆使用两个模型。``LocalHero``对象的ImageId使用的数据类型是整型Int，这样就方便设置图片的显示了。

从云端拿下来的图片，是以``文件下载``的方式，并不是一条数据拿下来就可以直接使用的，因为RecyclerView里面的适配器无法解析File类型的，与适配器对应的类的``LocalHero``，在``LocalHero``里，``ImageView``的存储形式是``ResourceId``，例如``R.drawwale.guanyu.jpg``这样的``int``值，所以将从云端下载的图片（``File``形式，如``guanyu.jpg``）以``Bitmap``的形式设置给``ImageView``控件，这样才行得通。

所以需要下载到本地，手机储存起来，再从手机读取。

区别：

hero/LocalHero.java：

```
    private String name;   //姓名
    
    private int heroImageId;
    
    private String sex;     //性别
    private String date;    //出生日期
    private String place;   //籍贯
    private String state;   //主校势力
    private String introduction;    //简介

    private int force;  //武力值(0~100)
    private int intelligence;  //智力值(0~100)
    private int leadership;   //统率值(0~100)
    private int army;   //军队数量:有统率值决定
    private int forage; // 粮草数量(0~1000)
```

entities/Hero.java:

```
    private String name;   //姓名
    
    private BmobFile heroImage;
    
    private String sex;     //性别
    private String date;    //出生日期
    private String place;   //籍贯
    private String state;   //主校势力
    private String introduction;    //简介

    private int force;  //武力值(0~100)
    private int intelligence;  //智力值(0~100)
    private int leadership;   //统率值(0~100)
    private int army;   //军队数量:有统率值决定
    private int forage; // 粮草数量(0~1000)
```

### 云端数据库

Bmob云端数据库也是一种面向对象的关系映射模型下的数据库，数据操作请自行查阅Bmob官方开发文档。

这里我们使用到了两个对象模型

```
    Hero.java  //云端英雄信息存储
    PKRecords.java  //英雄对决记录
```

### 本地数据库

采用``Litepal``库，而不用系统自带的``SQLite``，后者的操作语句有点繁琐，前者是面向对象的，是一种对象关系映射模型，直接对应数据库的表的行与列。
详细使用可以看《第一行代码（第二版）》或者自行搜索资料解决。

有三个对象模型

```
    LocalHero.java  //用于储存英雄资料信息
    PKRecords.java  //用于英雄对决记录
    MyHero.java  //这个还未添加，用于‘我的英雄’
```




# Get Started

项目目录

ThreeKingdoms/app/src/main/java/com/example/jiamoufang/threekingdoms/

```
  -- activities	//所有活动
    -- AddHero.java
    -- HeroDetailsActivity.java
  -- adapter  //所有适配器
    -- HeroAdapter.java
  -- entities  //所有的与云端数据库和本地数据库之间的关系映射的对象模型，即实体（entity）
    -- Hero.java
    -- PKRecords
  -- fragment	 //三个Fragment
    -- HerosListFragment.java	
    -- HerosPKFragment.java
    -- HitHeroFragment.java
  -- heros	 //人物英雄类
   -- Hero.java
  -- MainActivity.java
```





