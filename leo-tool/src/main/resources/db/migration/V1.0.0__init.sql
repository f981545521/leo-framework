-- ## Flyway注意事项
-- 1. 命名格式必须规范
--      1、仅需要执行一次的脚本，以V开头，后面跟上0~9的数字组合，数字之间可以使用.或者_进行分割。然后再以两个下划线 __进行分割，其后跟上文件名称，最后以.sql结尾。
--      2、需要重复执行的SQL，则需要以R开头。后面再以两个下划线分割，其后跟文件名称，最后以.sql结尾。
--          【注意】这里的 R 重复执行脚本，并不是说是启动项目后不断执行！而是，每次启动项目，都会重新校验对应的 R__add_user_info.sql 内容是否变更，如果变更则重新执行。
-- 2. 项目启动时，会执行db/migration目录下迁移文件
-- 3. 文件一旦执行后不能删除（必须将`flyway_schema_history`表中执行记录也删除）