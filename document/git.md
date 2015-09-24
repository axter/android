用户信息
git config --global user.name "axter"
git config --global user.email applestonedream@163.com
文本编辑器
git config --global core.editor vim
差异分析工具
git config --global merge.tool vimdiff
查看配置信息
git config --list


在工作目录中初始化新仓库
git init

git add

git commit -m ''


git clone xx xx(新的名字)

检查当前文件状态
git status

看暂存前后的变化
git diff

查看已经暂存起来的变化
git diff --cached 

提交更新
git commit

跳过git add,提交所有已经跟踪过的文件
git commit -a

从暂存区域移除文件
git rm -f(强制删除) --cached(删除版本管理,但不删文件)

文件改名
git mv file_from file_to

回顾提交历史
git log -p(展开显示每次提交的内容差异) -2(最近的两次更新) --stat(仅显示简要的增改行数统计)

修改最后一次提交,重新提交
git commit --amend

取消暂存文件
git reset HEAD xx

查看当前的远程库
git remote -v

添加远程库
git remote add xx
从远程仓库抓取数据
git fetch
git pull

git push origin[remote-name] [branch-name]

创建分支
git branch xx
切换分支(-b)
git checkout xx
删除分支
git branch -d xx
把xx分支合并到当前分支
git merge xx

git mergetool










or create a new repository on the command line


echo # android >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/axter/android.git
git push -u origin master

or push an existing repository from the command line


git remote add origin https://github.com/axter/android.git
git push -u origin master