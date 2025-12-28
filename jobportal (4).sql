-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2025 at 10:57 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jobportal`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `application`
--

CREATE TABLE `application` (
  `application_id` int(11) NOT NULL,
  `seeker_id` int(11) DEFAULT NULL,
  `job_id` int(11) DEFAULT NULL,
  `resume` text DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `application_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `application`
--

INSERT INTO `application` (`application_id`, `seeker_id`, `job_id`, `resume`, `status`, `application_date`) VALUES
(1, 3, 1, 'attached', 'Pending', '2025-12-04'),
(2, 3, 2, 'agskaigsd', 'Pending', '2025-12-15'),
(3, 4, 5, '=== COVER LETTER ===\nDear Hiring Manager,\n\nI am writing to express my interest in the Beauty Advisor position...\n\n=== RESUME FILE ===\nFile: Marzana Tabassum CV .pdf\nPath: C:\\Users\\USER\\OneDrive\\Documents\\Marzana Tabassum CV .pdf\nSize: 160 KB', 'Accepted', '2025-12-15'),
(4, 5, 7, '=== COVER LETTER ===\nDear Hiring Manager,\n\nI am writing to express my interest in the Head Chef position...\n\n=== RESUME FILE ===\nFile: Marzana Tabassum CV .pdf\nPath: F:\\Marzana Tabassum CV .pdf\nSize: 206 KB', 'Hired', '2025-12-15'),
(5, 6, 8, '=== COVER LETTER ===\nDear Hiring Manager,\n\nI am writing to express my interest in the Sales Executive (Female Preferred) position...\n\n=== RESUME FILE ===\nFile: Marzana Tabassum CV .pdf\nPath: F:\\Marzana Tabassum CV .pdf\nSize: 206 KB', 'Rejected', '2025-12-15'),
(6, 8, 9, '=== COVER LETTER ===\nDear Hiring Manager,\n\nI am writing to express my interest in the chef position...\n\n=== RESUME FILE ===\nFile: Marzana Tabassum CV .pdf\nPath: F:\\Marzana Tabassum CV .pdf\nSize: 206 KB', 'Interview Scheduled', '2025-12-16'),
(7, 9, 10, '=== COVER LETTER ===\nDear Hiring Manager,\n\nI am writing to express my interest in the Bakery Staff position...\n\n=== RESUME FILE ===\nFile: 15. Functional Dependency_NormalizationCh_14.pdf\nPath: C:\\Users\\USER\\Downloads\\15. Functional Dependency_NormalizationCh_14.pdf\nSize: 573 KB', 'Hired', '2025-12-16');

-- --------------------------------------------------------

--
-- Table structure for table `company`
--

CREATE TABLE `company` (
  `company_id` int(11) NOT NULL,
  `name` varchar(150) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `website` varchar(150) DEFAULT NULL,
  `detail` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `company`
--

INSERT INTO `company` (`company_id`, `name`, `email`, `password`, `phone`, `location`, `website`, `detail`) VALUES
(1, 'TedX', 'tedX@gmail.com', '123', '01949612244', 'Banani', 'www.tedx.com', 'tech firm'),
(2, 'Peony Beauty Bar', 'peony@gmail.com', '123', '01949612244', 'Gulshan', 'www.peonybeautybar.com', 'Beauty Salon'),
(3, 'GlamCart', 'glamcart@gmail.com', '123456', '01986821945', 'Banani', 'https://www.glamcart.com', 'Drugstore and high end makeup ,cosmetics and skin care shop.'),
(4, 'Saffron Flame Restaurant', 'saffron@gmail.com', '123456', '01817566296', 'Badda', 'https://', 'Cuisine: Continental • Asian • Bangladeshi Fusion'),
(5, 'SugarPlum Handbags', 'sugarplum@gmail.com', '123456', '01986821945', 'Tejgaon', 'https://www.sugarplum.com', 'Luxury Bags Shop'),
(6, 'b', 'b@gmail.com', '123456', '01908671741', 'banani', 'https://www.b.com', 'Describe your company...'),
(7, 'Flourish Bakery', 'flourish@gmail.com', '123456', '01949612244', 'Khilgaon', 'https://www.flourishbakery.com', 'Describe your company...');

-- --------------------------------------------------------

--
-- Table structure for table `company_review`
--

CREATE TABLE `company_review` (
  `review_id` int(11) NOT NULL,
  `seeker_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` >= 1 and `rating` <= 5),
  `review_text` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `company_review`
--

INSERT INTO `company_review` (`review_id`, `seeker_id`, `company_id`, `rating`, `review_text`, `created_at`) VALUES
(1, 4, 4, 5, 'Great Experience !', '2025-12-15 14:41:27'),
(2, 8, 6, 5, 'great', '2025-12-15 19:22:26'),
(3, 9, 6, 3, 'good Experience', '2025-12-15 21:48:17');

-- --------------------------------------------------------

--
-- Table structure for table `interview`
--

CREATE TABLE `interview` (
  `interview_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `seeker_id` int(11) NOT NULL,
  `interview_date` date NOT NULL,
  `interview_time` time NOT NULL,
  `location` varchar(500) DEFAULT NULL,
  `type` varchar(50) DEFAULT 'In-person',
  `meeting_link` varchar(500) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `status` varchar(50) DEFAULT 'Scheduled',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `interview`
--

INSERT INTO `interview` (`interview_id`, `application_id`, `company_id`, `seeker_id`, `interview_date`, `interview_time`, `location`, `type`, `meeting_link`, `notes`, `status`, `created_at`) VALUES
(1, 4, 4, 5, '2025-12-20', '10:00:00', 'Company Office', 'In-person', 'https://meet.google.com/...', 'Please bring your portfolio and relevant documents.', 'Done', '2025-12-15 07:15:29'),
(2, 5, 5, 6, '2025-12-20', '10:00:00', 'Company Office', 'In-person', 'https://meet.google.com/...', 'Please bring your portfolio and relevant documents.', 'Scheduled', '2025-12-15 07:41:47'),
(3, 6, 4, 8, '2025-12-20', '10:00:00', 'Company Office', 'In-person', 'https://meet.google.com/...', 'Please bring your portfolio and relevant documents.', 'Scheduled', '2025-12-15 19:26:40'),
(4, 7, 7, 9, '2025-12-20', '10:00:00', 'Company Office', 'In-person', 'https://meet.google.com/...', 'Please bring your portfolio and relevant documents.', 'Done', '2025-12-15 21:53:20');

-- --------------------------------------------------------

--
-- Table structure for table `job`
--

CREATE TABLE `job` (
  `job_id` int(11) NOT NULL,
  `company_id` int(11) DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `title` varchar(150) DEFAULT NULL,
  `skills` text DEFAULT NULL,
  `experience` text DEFAULT NULL,
  `salary_range` varchar(100) DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `vacancy` int(11) DEFAULT NULL,
  `posted_date` date DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `platform` varchar(50) DEFAULT NULL,
  `requirement` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `job`
--

INSERT INTO `job` (`job_id`, `company_id`, `category_id`, `title`, `skills`, `experience`, `salary_range`, `deadline`, `vacancy`, `posted_date`, `location`, `platform`, `requirement`) VALUES
(1, 1, 1, 'Looking for Engineers', 'App Development', '2 years', '80000-850000', '2025-01-01', 4, '2025-12-04', 'Banani', 'Onsite', 'BSC.in CSE CGPA 3.00 or above'),
(2, 1, 1, 'Business Analyst', 'Analytical skills', '0', '70,000-80,000', '2025-12-30', 1, '2025-12-15', 'Remote', 'Online', 'None'),
(3, 2, 2, 'Hair Stylist', 'Blow-dry & heat styling techniques', '2 years', '20000-25000', '2025-12-20', 1, '2025-12-15', 'Gulshan', 'Onsite', 'Bachelor complete'),
(4, 2, 1, 'Makeup Artist', 'Color correction & contouring', '0', '10,000-12000', '2025-12-30', 1, '2025-12-15', 'Remote', 'Online', 'None'),
(5, 3, 1, 'Beauty Advisor', 'Shade matching & skin analysis', '0-2 years', '15000', '2025-12-31', 2, '2025-12-15', 'Gulshan', 'Onsite', 'BA in Fashion Designing'),
(6, 3, 1, 'product specialist', 'product knowledge', '0-2 years', '10000-12000', '2025-12-31', 1, '2025-12-15', 'gulshan', 'Remote', 'bba'),
(7, 4, 1, 'Head Chef', 'Plan and prepare menus', '0-2 years', '30000-32000', '2025-12-31', 2, '2025-12-15', 'Badda', 'Onsite', 'Diploma/Certificate in Culinary Arts'),
(8, 5, 2, 'Sales Executive (Female Preferred)', 'Good communication skills   and Interest in fashion & accessories', '0-2 years', '15000-16000', '2025-12-31', 5, '2025-12-15', 'Tejgaon outlet', 'Onsite', 'Minimum HSC'),
(9, 4, 1, 'chef', 'cooking', '0-2 years', '10000', '2025-12-31', 1, '2025-12-16', 'banani', 'Onsite', 'bba'),
(10, 7, 1, 'Bakery Staff', 'Baking', '0-2 years', '10000', '2025-12-31', 1, '2025-12-16', 'banani', 'Onsite', 'BA');

-- --------------------------------------------------------

--
-- Table structure for table `jobcategory`
--

CREATE TABLE `jobcategory` (
  `category_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `detail` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jobcategory`
--

INSERT INTO `jobcategory` (`category_id`, `name`, `detail`) VALUES
(1, 'Software Development', 'Programming and software engineering roles'),
(2, 'Marketing', 'Marketing, advertising, and brand management'),
(3, 'Sales', 'Sales and business development positions'),
(4, 'Design', 'UI/UX, graphic design, and creative roles'),
(5, 'Human Resources', 'HR, recruitment, and people management'),
(6, 'Finance', 'Accounting, finance, and investment roles'),
(7, 'Customer Service', 'Customer support and service positions'),
(8, 'Data Science', 'Data analysis, AI, and machine learning roles');

-- --------------------------------------------------------

--
-- Table structure for table `jobseeker`
--

CREATE TABLE `jobseeker` (
  `seeker_id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `experience` text DEFAULT NULL,
  `skills` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jobseeker`
--

INSERT INTO `jobseeker` (`seeker_id`, `name`, `email`, `password`, `phone`, `address`, `experience`, `skills`) VALUES
(1, 'Marzana ', 'Marzana@gmail.com', '456', '01949612244', 'Khilgaon', '2 years', 'Programming'),
(2, 'Turza', 'turza@gmail.com', '789', '01531767210', 'Mirpur', '3 years', 'Gaming ,Programming,App Developing'),
(3, 'Rifat', 'rifat@yahoo.com', 'abc', '01531767210', 'Gulshan', '2 years ', 'Hardware management ,software engineering'),
(4, 'progga', 'progga@gmail.com', 'abc', '01908571741', 'Mirpur', '1 year', 'Hair styling,Manicure pedicure'),
(5, 'Will Byers', 'will@gmail.com', '123456', '01949612244', 'Hawkins', '0 years', 'Baking'),
(6, 'Maixne Mayfield', 'maxine@gmail.com', '123456', '01864567342', 'California', '2 years', 'sales executing'),
(8, 'turza', 'turza@yahoo.com', '123456', '01531767210', 'mirpur', '0 years', 'coding and gaming'),
(9, 'Tultul', 'tultul@gmail.com', '123456', '01949612244', 'khilgaon', '0 years', 'sales executive');

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE `notification` (
  `notification_id` int(11) NOT NULL,
  `seeker_id` int(11) NOT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  `job_id` int(11) DEFAULT NULL,
  `message` text DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`notification_id`, `seeker_id`, `company_name`, `job_title`, `job_id`, `message`, `is_read`, `created_at`) VALUES
(1, 3, 'TedX', 'Business Analyst', 2, 'New job posted by TedX: Business Analyst', 0, '2025-12-14 19:10:50'),
(2, 4, 'Peony Beauty Bar', 'Makeup Artist', 4, 'New job posted by Peony Beauty Bar: Makeup Artist', 1, '2025-12-14 19:26:41'),
(3, 4, 'GlamCart', 'Beauty Advisor', 5, 'New job posted by GlamCart: Beauty Advisor', 0, '2025-12-14 20:19:57'),
(4, 4, 'GlamCart', 'product specialist', 6, 'New job posted by GlamCart: product specialist', 0, '2025-12-15 00:26:44'),
(5, 5, 'Saffron Flame Restaurant', 'Head Chef', 7, 'New job posted by Saffron Flame Restaurant: Head Chef', 0, '2025-12-15 07:10:44'),
(6, 6, 'SugarPlum Handbags', 'Sales Executive (Female Preferred)', 8, 'New job posted by SugarPlum Handbags: Sales Executive (Female Preferred)', 0, '2025-12-15 07:37:35'),
(7, 5, 'Saffron Flame Restaurant', 'chef', 9, 'New job posted by Saffron Flame Restaurant: chef', 0, '2025-12-15 19:24:19'),
(8, 8, 'Saffron Flame Restaurant', 'chef', 9, 'New job posted by Saffron Flame Restaurant: chef', 0, '2025-12-15 19:24:19'),
(9, 9, 'Flourish Bakery', 'Bakery Staff', 10, 'New job posted by Flourish Bakery: Bakery Staff', 0, '2025-12-15 21:50:21');

-- --------------------------------------------------------

--
-- Table structure for table `premium_features`
--

CREATE TABLE `premium_features` (
  `feature_id` int(11) NOT NULL,
  `seeker_id` int(11) NOT NULL,
  `feature_type` varchar(50) NOT NULL,
  `payment_status` varchar(20) DEFAULT 'Not Paid',
  `payment_amount` decimal(10,2) DEFAULT NULL,
  `activated_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `premium_features`
--

INSERT INTO `premium_features` (`feature_id`, `seeker_id`, `feature_type`, `payment_status`, `payment_amount`, `activated_date`) VALUES
(1, 4, 'REVIEW_FEATURE', 'Paid', 9.99, '2025-12-15 14:40:43'),
(2, 4, 'PROFILE_EDIT', 'Paid', 14.99, '2025-12-15 14:48:24'),
(3, 8, 'REVIEW_FEATURE', 'Paid', 9.99, '2025-12-15 19:21:52'),
(4, 8, 'PROFILE_EDIT', 'Paid', 14.99, '2025-12-15 19:22:34'),
(5, 9, 'PROFILE_EDIT', 'Paid', 14.99, '2025-12-15 21:47:10'),
(6, 9, 'REVIEW_FEATURE', 'Paid', 9.99, '2025-12-15 21:47:39');

-- --------------------------------------------------------

--
-- Table structure for table `resume_access_log`
--

CREATE TABLE `resume_access_log` (
  `log_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `application_id` int(11) DEFAULT NULL,
  `action` varchar(100) NOT NULL,
  `success` tinyint(1) NOT NULL,
  `access_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resume_access_log`
--

INSERT INTO `resume_access_log` (`log_id`, `company_id`, `application_id`, `action`, `success`, `access_time`) VALUES
(1, 3, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-14 20:51:10'),
(2, 3, 3, 'VIEW_CONTENT', 1, '2025-12-14 20:51:51'),
(3, 3, 3, 'VIEW_RESUME', 1, '2025-12-14 20:51:56'),
(4, 3, 3, 'UPDATE_STATUS:Accepted', 1, '2025-12-14 20:52:11'),
(5, 3, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-14 20:52:13'),
(6, 3, 3, 'VIEW_CONTENT', 1, '2025-12-14 20:52:19'),
(7, 3, 3, 'VIEW_RESUME', 1, '2025-12-14 20:52:25'),
(8, 3, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-14 20:56:53'),
(9, 3, 3, 'VIEW_CONTENT', 1, '2025-12-14 20:57:01'),
(10, 3, 3, 'VIEW_RESUME', 1, '2025-12-14 20:57:03'),
(11, 3, 3, 'VIEW_CONTENT', 1, '2025-12-14 20:57:17'),
(12, 1, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-14 21:02:38'),
(13, 1, 2, 'VIEW_CONTENT', 1, '2025-12-14 21:02:51'),
(14, 1, 2, 'VIEW_RESUME', 1, '2025-12-14 21:02:54'),
(15, 3, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 00:25:45'),
(16, 4, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:14:31'),
(17, 4, 4, 'VIEW_CONTENT', 1, '2025-12-15 07:15:00'),
(18, 4, 4, 'VIEW_RESUME', 1, '2025-12-15 07:15:02'),
(19, 4, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:15:34'),
(20, 4, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:26:24'),
(21, 4, 4, 'VIEW_CONTENT', 1, '2025-12-15 07:26:37'),
(22, 4, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:26:43'),
(23, 5, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:39:21'),
(24, 5, 5, 'VIEW_CONTENT', 1, '2025-12-15 07:39:23'),
(25, 5, 5, 'VIEW_CONTENT', 1, '2025-12-15 07:39:26'),
(26, 5, 5, 'VIEW_RESUME', 1, '2025-12-15 07:41:17'),
(27, 5, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:41:54'),
(28, 5, 5, 'VIEW_CONTENT', 1, '2025-12-15 07:42:03'),
(29, 5, 5, 'UPDATE_STATUS:Rejected', 1, '2025-12-15 07:42:11'),
(30, 5, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 07:42:13'),
(31, 4, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 19:26:11'),
(32, 4, 6, 'VIEW_CONTENT', 1, '2025-12-15 19:26:18'),
(33, 4, 6, 'VIEW_CONTENT', 1, '2025-12-15 19:26:22'),
(34, 4, 6, 'VIEW_RESUME', 1, '2025-12-15 19:26:37'),
(35, 4, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 19:26:43'),
(36, 7, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 21:53:00'),
(37, 7, 7, 'VIEW_CONTENT', 1, '2025-12-15 21:53:02'),
(38, 7, 7, 'VIEW_RESUME', 1, '2025-12-15 21:53:11'),
(39, 7, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 21:53:25'),
(40, 7, 7, 'VIEW_CONTENT', 1, '2025-12-15 21:53:31'),
(41, 7, -1, 'VIEW_ALL_APPLICATIONS', 1, '2025-12-15 21:53:40');

-- --------------------------------------------------------

--
-- Table structure for table `subscription`
--

CREATE TABLE `subscription` (
  `subscription_id` int(11) NOT NULL,
  `seeker_id` int(11) NOT NULL,
  `company_id` int(11) NOT NULL,
  `subscribed_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `subscription`
--

INSERT INTO `subscription` (`subscription_id`, `seeker_id`, `company_id`, `subscribed_date`) VALUES
(1, 3, 1, '2025-12-14 19:08:09'),
(2, 4, 2, '2025-12-14 19:24:31'),
(3, 4, 3, '2025-12-14 20:15:09'),
(4, 5, 4, '2025-12-15 07:08:52'),
(5, 6, 5, '2025-12-15 07:35:22'),
(6, 8, 4, '2025-12-15 19:23:04'),
(7, 9, 7, '2025-12-15 21:46:43');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`);

--
-- Indexes for table `application`
--
ALTER TABLE `application`
  ADD PRIMARY KEY (`application_id`),
  ADD KEY `application_ibfk_1` (`seeker_id`),
  ADD KEY `application_ibfk_2` (`job_id`);

--
-- Indexes for table `company`
--
ALTER TABLE `company`
  ADD PRIMARY KEY (`company_id`);

--
-- Indexes for table `company_review`
--
ALTER TABLE `company_review`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `seeker_id` (`seeker_id`),
  ADD KEY `company_id` (`company_id`);

--
-- Indexes for table `interview`
--
ALTER TABLE `interview`
  ADD PRIMARY KEY (`interview_id`),
  ADD KEY `application_id` (`application_id`),
  ADD KEY `company_id` (`company_id`),
  ADD KEY `seeker_id` (`seeker_id`);

--
-- Indexes for table `job`
--
ALTER TABLE `job`
  ADD PRIMARY KEY (`job_id`),
  ADD KEY `job_ibfk_1` (`company_id`),
  ADD KEY `job_ibfk_2` (`category_id`);

--
-- Indexes for table `jobcategory`
--
ALTER TABLE `jobcategory`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `jobseeker`
--
ALTER TABLE `jobseeker`
  ADD PRIMARY KEY (`seeker_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`notification_id`),
  ADD KEY `seeker_id` (`seeker_id`);

--
-- Indexes for table `premium_features`
--
ALTER TABLE `premium_features`
  ADD PRIMARY KEY (`feature_id`),
  ADD UNIQUE KEY `unique_feature` (`seeker_id`,`feature_type`);

--
-- Indexes for table `resume_access_log`
--
ALTER TABLE `resume_access_log`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `idx_company` (`company_id`),
  ADD KEY `idx_application` (`application_id`),
  ADD KEY `idx_time` (`access_time`);

--
-- Indexes for table `subscription`
--
ALTER TABLE `subscription`
  ADD PRIMARY KEY (`subscription_id`),
  ADD UNIQUE KEY `unique_subscription` (`seeker_id`,`company_id`),
  ADD KEY `company_id` (`company_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `application`
--
ALTER TABLE `application`
  MODIFY `application_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `company`
--
ALTER TABLE `company`
  MODIFY `company_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `company_review`
--
ALTER TABLE `company_review`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `interview`
--
ALTER TABLE `interview`
  MODIFY `interview_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `job`
--
ALTER TABLE `job`
  MODIFY `job_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `jobcategory`
--
ALTER TABLE `jobcategory`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `jobseeker`
--
ALTER TABLE `jobseeker`
  MODIFY `seeker_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `notification`
--
ALTER TABLE `notification`
  MODIFY `notification_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `premium_features`
--
ALTER TABLE `premium_features`
  MODIFY `feature_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `resume_access_log`
--
ALTER TABLE `resume_access_log`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `subscription`
--
ALTER TABLE `subscription`
  MODIFY `subscription_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `application`
--
ALTER TABLE `application`
  ADD CONSTRAINT `application_ibfk_1` FOREIGN KEY (`seeker_id`) REFERENCES `jobseeker` (`seeker_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `application_ibfk_2` FOREIGN KEY (`job_id`) REFERENCES `job` (`job_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `company_review`
--
ALTER TABLE `company_review`
  ADD CONSTRAINT `company_review_ibfk_1` FOREIGN KEY (`seeker_id`) REFERENCES `jobseeker` (`seeker_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `company_review_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE;

--
-- Constraints for table `interview`
--
ALTER TABLE `interview`
  ADD CONSTRAINT `interview_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `application` (`application_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `interview_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `interview_ibfk_3` FOREIGN KEY (`seeker_id`) REFERENCES `jobseeker` (`seeker_id`) ON DELETE CASCADE;

--
-- Constraints for table `job`
--
ALTER TABLE `job`
  ADD CONSTRAINT `job_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `job_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `jobcategory` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`seeker_id`) REFERENCES `jobseeker` (`seeker_id`) ON DELETE CASCADE;

--
-- Constraints for table `premium_features`
--
ALTER TABLE `premium_features`
  ADD CONSTRAINT `premium_features_ibfk_1` FOREIGN KEY (`seeker_id`) REFERENCES `jobseeker` (`seeker_id`) ON DELETE CASCADE;

--
-- Constraints for table `resume_access_log`
--
ALTER TABLE `resume_access_log`
  ADD CONSTRAINT `resume_access_log_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE;

--
-- Constraints for table `subscription`
--
ALTER TABLE `subscription`
  ADD CONSTRAINT `subscription_ibfk_1` FOREIGN KEY (`seeker_id`) REFERENCES `jobseeker` (`seeker_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `subscription_ibfk_2` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
