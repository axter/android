#!/usr/bin/python
# -*- coding: UTF-8 -*- 

# 基于python 3.5 实现
# 基本功能
# xx_xx_xx.apk => 根据列表打channel渠道包
# 使用方法
# 1,程序 脚本 输入目录 [,规则文件]=> aa同级目录会生成aa_out的输出文件夹
# 1,例如 python input.py E:\input\aa\aa.apk [,E:\input\aa\input.txt]
# 2,默认脚本下存在input.txt文件,一行一条形式存在

import zipfile , os , shutil , random , sys

if len(sys.argv) < 2:
	print ('缺少参数')
	sys.exit()

# 变量
py_home = os.path.dirname(os.path.realpath(sys.argv[0]))
out_dir = ""
input_apk_name = ""
input_apk = ""
input_txt = py_home + os.sep + "input.txt"
meta_inf_dir = py_home + os.sep + "META-INF"
channel_ = "channel_{channel}"
# 输入文件地址
input_apk = sys.argv[1]
# 规则文件地址
if len(sys.argv) > 2:
	input_txt = sys.argv[2]
	
if not os.path.isfile(input_apk):
	print ('输入文件不存在')
	sys.exit()
if not os.path.isfile(input_txt):
	print ('规则文件不存在')
	sys.exit()

apks = os.path.split(input_apk)
input_apk_name = os.path.splitext(apks[1])[0]
out_dir = apks[0] + os.sep + input_apk_name


# 删除临时文件夹
shutil.rmtree(out_dir,True)
shutil.rmtree(meta_inf_dir,True)
# 创建临时文件夹
if not os.path.exists(out_dir):
	os.mkdir(out_dir)
if not os.path.exists(meta_inf_dir):
	os.mkdir(meta_inf_dir)

index = 0
for line in open(input_txt):
	index += 1
	# 读文件
	line = line.rstrip()
	if len(line) == 0:
		print ("line null" , str(index))
		continue

	channel_name = channel_.format(channel=line)
	channel_file = meta_inf_dir + os.sep + channel_name
	zip_file = out_dir + os.sep + input_apk_name + "_" +  line + ".apk"
	# 拷贝apk文件
	if os.path.exists(zip_file):
		zip_file = out_dir + os.sep + input_apk_name + "_" + line + "_" + str(index) + ".apk"
	open(zip_file, "wb").write(open(input_apk, "rb").read())

	# 新建channel
	open(channel_file, 'w').close()
	# 写zip文件
	zip = zipfile.ZipFile(zip_file, 'a', zipfile.ZIP_DEFLATED)
	zip.write(channel_file,"META-INF" + os.sep + channel_name)
	zip.close