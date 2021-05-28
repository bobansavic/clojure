USE [agilityTestDb]

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ProjectsUsers]') AND type in (N'U'))
BEGIN

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Project]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Project](
	[id] [bigint] NOT NULL,
	[title] [varchar](255) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_9ytiqf3wdk0iv4jcttcngwr9a] UNIQUE NONCLUSTERED
(
	[title] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ProjectsUsers]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[ProjectsUsers](
	[project_id] [bigint] NOT NULL,
	[user_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED
(
	[project_id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Task]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Task](
	[id] [bigint] NOT NULL,
	[description] [varchar](255) NULL,
	[priority] [varchar](255) NULL,
	[status] [varchar](255) NULL,
	[title] [varchar](255) NULL,
	[assignee_id] [bigint] NULL,
	[project_id] [bigint] NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_ckvnvh6xmm7ihmid6bfx1jg3b] UNIQUE NONCLUSTERED
(
	[title] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[UserRole]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[UserRole](
	[id] [bigint] NOT NULL,
	[name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Users](
	[id] [bigint] NOT NULL,
	[email] [varchar](255) NULL,
	[firstName] [varchar](255) NULL,
	[lastName] [varchar](255) NULL,
	[password] [varchar](255) NULL,
	[username] [varchar](255) NULL,
	[role_id] [bigint] NULL,
PRIMARY KEY CLUSTERED
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_23y4gd49ajvbqgl3psjsvhff6] UNIQUE NONCLUSTERED
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_ncoa9bfasrql0x4nhmh1plxxy] UNIQUE NONCLUSTERED
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Project]') AND type in (N'U'))
BEGIN
INSERT [dbo].[Project] ([id], [title]) VALUES (8, N'Test project')
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ProjectsUsers]') AND type in (N'U'))
BEGIN
INSERT [dbo].[ProjectsUsers] ([project_id], [user_id]) VALUES (8, 6)
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Task]') AND type in (N'U'))
BEGIN
INSERT [dbo].[Task] ([id], [description], [priority], [status], [title], [assignee_id], [project_id]) VALUES (9, N'Testasacsaascassascsf
ff', N'High', N'Testing', N'Neki novi hitni story', 7, 8)
INSERT [dbo].[Task] ([id], [description], [priority], [status], [title], [assignee_id], [project_id]) VALUES (10, N'sadas', N'Low', N'In progress', N'Nesto novo', 7, 8)
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[UserRole]') AND type in (N'U'))
BEGIN
INSERT [dbo].[UserRole] ([id], [name]) VALUES (1, N'ROLE_ADMIN')
INSERT [dbo].[UserRole] ([id], [name]) VALUES (2, N'ROLE_USER')
INSERT [dbo].[UserRole] ([id], [name]) VALUES (3, N'ROLE_PROJECT_MANAGER')
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (4, N'admin@agility.com', N'Admin', N'Admin', N'admin', N'admin', 1)
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (5, N'pm@agility.com', N'Project', N'Manager', N'pm', N'pm', 3)
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (6, N'test@test.com', N'Test', N'User', N'test', N'testuser', 2)
INSERT [dbo].[Users] ([id], [email], [firstName], [lastName], [password], [username], [role_id]) VALUES (7, N'bobanzsavic@gmail.com', N'Boban', N'Savic', N'test', N'bsavic', 2)
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ProjectsUsers]') AND type in (N'U'))
BEGIN
ALTER TABLE [dbo].[ProjectsUsers]  WITH CHECK ADD  CONSTRAINT [FK17rp8yxv8raud3bcabap7r2ei] FOREIGN KEY([project_id])
REFERENCES [dbo].[Project] ([id])

ALTER TABLE [dbo].[ProjectsUsers] CHECK CONSTRAINT [FK17rp8yxv8raud3bcabap7r2ei]

ALTER TABLE [dbo].[ProjectsUsers]  WITH CHECK ADD  CONSTRAINT [FKcln7da8bvb59555x6ghjgkdkh] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([id])

ALTER TABLE [dbo].[ProjectsUsers] CHECK CONSTRAINT [FKcln7da8bvb59555x6ghjgkdkh]
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Task]') AND type in (N'U'))
BEGIN
ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FKkkcat6aybe3nbvhc54unstxm6] FOREIGN KEY([project_id])
REFERENCES [dbo].[Project] ([id])

ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FKkkcat6aybe3nbvhc54unstxm6]

ALTER TABLE [dbo].[Task]  WITH CHECK ADD  CONSTRAINT [FKm58ppaj5k88gvxwcnn9n8dbtt] FOREIGN KEY([assignee_id])
REFERENCES [dbo].[Users] ([id])

ALTER TABLE [dbo].[Task] CHECK CONSTRAINT [FKm58ppaj5k88gvxwcnn9n8dbtt]
END

IF EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
ALTER TABLE [dbo].[Users]  WITH CHECK ADD  CONSTRAINT [FK1erb0m3aye7jwmc1a6l42dr7p] FOREIGN KEY([role_id])
REFERENCES [dbo].[UserRole] ([id])

ALTER TABLE [dbo].[Users] CHECK CONSTRAINT [FK1erb0m3aye7jwmc1a6l42dr7p]
END

END
