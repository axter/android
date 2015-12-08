#!/usr/bin/python
# -*- coding: UTF-8 -*- 

# 基于python 3.5 实现
# 基本功能
# xx_aa_bb.apk => 根据aa名查询value设置channel渠道
# 使用方法
# 1,程序 脚本 输入目录 [,规则文件]=> aa同级目录会生成aa_out的输出文件夹
# 1,例如 python change.py E:\change\aa [,E:\change\aa\change.txt]
# 2,默认脚本下存在change.txt文件,key##value形式存在

import zipfile , os , shutil , random , sys

if len(sys.argv) < 2:
	print ('缺少参数')
	sys.exit()
# 变量
py_home = os.path.dirname(os.path.realpath(sys.argv[0]))
out_dir = ""
input_dir = ""
input_txt = py_home + os.sep + "change.txt"
meta_inf_dir = py_home + os.sep + "META-INF"
channel_ = "channel_{channel}"
# 赋值变量
input_dir = sys.argv[1]
if len(sys.argv) > 2:
	input_txt = sys.argv[2]

if not os.path.isdir(input_dir):
	print ('输入文件夹不存在')
	sys.exit()
if not os.path.isfile(input_txt):
	print ('规则文件不存在')
	sys.exit()

input_dir_parent_dir = os.path.dirname(input_dir)
input_dir_name = os.path.basename(input_dir)
out_dir = input_dir_parent_dir + os.sep + input_dir_name + "_out"

# 删除临时文件夹
shutil.rmtree(out_dir,True)
shutil.rmtree(meta_inf_dir,True)
# 创建临时文件夹
if not os.path.exists(out_dir):
	os.mkdir(out_dir)
if not os.path.exists(meta_inf_dir):
	os.mkdir(meta_inf_dir)

# 生成对应数组
dicts = {}
for line in open(input_txt):
	kvs = line.split('##')
	key = kvs[0].rstrip()
	value = kvs[1].rstrip()
	if len(key)!=0:
		if len(value)!=0:
			dicts[key] = value

# 遍历apk文件
filelist = os.listdir(input_dir)
for num in range(len(filelist)):
	filename = filelist[num]
	filepath = input_dir + os.sep + filename
	if not os.path.isfile(filepath):
		print("文件不存在:" + filepath)
		continue
	filename_sp = filename.split('_')
	if len(filename_sp) < 2:
		print("文件名不规则:" + filename)
		continue
	key = filename_sp[1]
	if key in dicts:# 字典存在
		channel_name = channel_.format(channel=str(dicts[key]))
		channel_file = meta_inf_dir + os.sep + channel_name
		zip_file = out_dir + os.sep + filename
		# 拷贝apk文件
		if os.path.exists(zip_file):
			print("重复的apk line:" + str(num))
			continue
		open(zip_file, "wb").write(open(filepath, "rb").read())
		# 新建channel
		open(channel_file, 'w').close()
		# 写zip文件
		zip = zipfile.ZipFile(zip_file, 'a', zipfile.ZIP_DEFLATED)
		zip.write(channel_file,"META-INF" + os.sep + channel_name)
		zip.close
	else:
		print("字典不存在:" + key)