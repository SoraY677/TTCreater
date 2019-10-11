# システム概要

組織や団体でのシフトを製作するのが面倒だという人向けに自動でシフトを製作してくれるアプリケーションです．組織や団体に所属する各個人からのシフト入力を集計し，そこに組織や団体の管理者(シフト製作者)が様々な条件を付与することで自動的にシフトを製作してくれます．
参考にするソフトウェアについては今のところ特に考えていませんが，参考にさせていただくシステムのアルゴリズムとして「進化計算アルゴリズムのGA」を使用させていただく予定です．

# システム詳細
## タスク一覧
[x] システムイメージ
[ ] DB設計
[ ] デザイン設計
[x] ログイン処理
[ ] GA設計・実装
[ ] 操作説明所作成
[ ] 動画撮影・投稿
# システム設計
----------
## ファイル構造
    com-example-test1(root) -AccessAuth
                            - shiftmaker
                                -DBFroms
----------
## クラス一覧
----------

**UserAuthRepository :authDB**

- ユーザー認証を行う際のSQL管理

**UserAuthTableForm :authDB**

- SQLのデータ取得用

**UseDetailsServiceImpl**

- ユーザ


## システムイメージ図
![](https://paper-attachments.dropbox.com/s_1D6DA84C28F118D83D0C2ACCB34942A5799B59648BFAE33A68AF3240E1BE71B7_1570269945363_1.png)

## DB設計
----------

**user_auth_table**

|      | user_id         | user_password |
| ---- | --------------- | ------------- |
| 型    | TEXT            | TEXT          |
| 制約条件 | NOT NULL / 12文字 | NOT NULL      |
| 例    | 123456000000    | tarou_pass    |

| user_id       | ユーザのID<br>**※12桁となる** |
| ------------- | --------------------- |
| user_password | ユーザーのパスワード            |

----------

**user_info_table**

|      | user_id         | user_name | user_status |
| ---- | --------------- | --------- | ----------- |
| 型    | TEXT            | TEXT      | INT         |
| 制約条件 | NOT NULL / 12文字 | NOT NULL  | NOT NULL    |
| 例    | 123456789010    | tarou     | 1           |

| user_id     | ユーザのID<br>**※12桁となる**  |
| ----------- | ---------------------- |
| user_name   | ユーザーの名前                |
| user_status | ユーザのモード<br>0:管理者/1:所属者 |

----------

**shift_timeel_config_table**

|      | user_id         | time_element |
| ---- | --------------- | ------------ |
| 型    | TEXT            | INT          |
| 制約条件 | NOT NULL / 12文字 | NOT NULL     |
| 例    | 123456789010    | 30           |

| user_id      | ユーザのID<br>**※12桁となる**           |
| ------------ | ------------------------------- |
| time_element | 単位時間:分単位<br>(30分ごとのシフトならば30になる) |

----------

**shift_config_table**

|      | user_id         | start_time | end_time | time_per_person | job      |
| ---- | --------------- | ---------- | -------- | --------------- | -------- |
| 型    | TEXT            | INT        | INT      | INT             | TEXT     |
| 制約条件 | NOT NULL / 12文字 | NOT NULL   | NOT NULL | NOT NULL        | NOT NULL |
| 例    | 123456789010    | 30         | 150      | 240             | キッチン，ホール |



| user_id         | ユーザのID<br>**※12桁となる** |
| --------------- | --------------------- |
| start_time      | 開始時間(分)               |
| end_time        | 終了時間(分)               |
| time_per_person | 一人当たりの最大仕事時間          |

----------

**shift_time_control_table**

|      | user_id         | date       | shift_details  |
| ---- | --------------- | ---------- | -------------- |
| 型    | TEXT            | INT        | TEXT           |
| 制約条件 | NOT NULL / 12文字 | NOT NULL   | NOT NULL       |
| 例    | 123456789010    | 2019-12-30 | ※Json形式．以下の図参照 |



| user_id       | ユーザのID<br>**※12桁となる**  |
| ------------- | ---------------------- |
| date          | 日付                     |
| shift_details | シフトをJson形式で表したデータ．以下参照 |

    {
    shift_details:[    
    {name:\"tarou\",shift:\"kitchin,kitchin,kitchin,none\"}
    ] 
    }
----------

**shift_apply_table**

|      | user_id         | date       | shift_details |
| ---- | --------------- | ---------- | ------------- |
| 型    | TEXT            | INT        | TEXT          |
| 制約条件 | NOT NULL / 12文字 | NOT NULL   | NOT NULL      |
| 例    | 123456789010    | 2019-12-30 | 以下参照          |



    "kitchin,kitchin,kithin,kithin"
