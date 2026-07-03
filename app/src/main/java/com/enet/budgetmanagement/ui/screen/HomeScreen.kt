package com.enet.budgetmanagement.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.enet.budgetmanagement.ui.theme.BudgetManagementTheme
import java.text.NumberFormat
import java.util.Locale

data class Transaction(
    val id: String,
    val title: String,
    val amount: Long,           // مثبت = درآمد، منفی = هزینه
    val date: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTransactionClick: (Transaction) -> Unit = {},
    onFabClick: () -> Unit = {}
) {
    val isPersian = isPersianLocale()
    val numberFormatter = NumberFormat.getNumberInstance(if (isPersian) Locale("fa", "IR") else Locale.US)

    BudgetManagementTheme() {
        Scaffold(
            topBar = { HomeTopBar(isPersian) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "افزودن تراکنش")
                }
            },
            bottomBar = { BottomNavigationBar(isPersian) }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // سلام و تاریخ
                item { GreetingSection(isPersian) }

                // کارت تراز ماه
                item { BalanceCard(isPersian, numberFormatter) }

                // سه کارت خلاصه
                item { SummaryCardsRow(isPersian, numberFormatter) }

                // لیست تراکنش‌های اخیر
                item {
                    Text(
                        text = if (isPersian) "آخرین تراکنش‌ها" else "Recent Transactions",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(getDummyTransactions()) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        isPersian = isPersian,
                        formatter = numberFormatter,
                        onClick = onTransactionClick
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) } // فضای خالی برای FAB
            }
        }
    }
}

// ==================== Top Bar ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(isPersian: Boolean) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (isPersian) "مدیریت هزینه‌ها" else "Expense Management",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* تغییر تم */ }) {
                Icon(Icons.Default.Brightness6, contentDescription = "تغییر تم")
            }
        },
        actions = {
            IconButton(onClick = { /* نوتیفیکیشن */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "نوتیفیکیشن")
            }
        }
    )
}

// ==================== سلام ====================
@Composable
private fun GreetingSection(isPersian: Boolean) {
    Column {
        Text(
            text = if (isPersian) "سلام، سینا 👋" else "Hi, Sina 👋",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = if (isPersian) "چهارشنبه ۱۰ تیر ۱۴۰۳" else "Wednesday, July 1, 2026",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ==================== کارت تراز ====================
@Composable
private fun BalanceCard(isPersian: Boolean, formatter: NumberFormat) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = if (isPersian) "تراز این ماه" else "This Month Balance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "${formatter.format(12540000)} تومان",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "↑ ۱۲٪",
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = if (isPersian) " نسبت به ماه قبل" else " vs last month",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

// ==================== سه کارت کوچک ====================
@Composable
private fun SummaryCardsRow(isPersian: Boolean, formatter: NumberFormat) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SummarySmallCard(
            title = if (isPersian) "درآمد این ماه" else "Income",
            value = 25800000,
            isIncome = true,
            formatter = formatter,
            modifier = Modifier.weight(1f)
        )
        SummarySmallCard(
            title = if (isPersian) "هزینه این ماه" else "Expense",
            value = -13260000,
            isIncome = false,
            formatter = formatter,
            modifier = Modifier.weight(1f)
        )
        SummarySmallCard(
            title = if (isPersian) "تراکنش‌ها" else "Transactions",
            count = 42,
            formatter = formatter,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummarySmallCard(
    title: String,
    value: Long? = null,
    count: Int? = null,
    isIncome: Boolean = false,
    formatter: NumberFormat,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(12.dp))

            if (value != null) {
                Text(
                    text = "${formatter.format(value)} تومان",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            } else if (count != null) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

// ==================== آیتم تراکنش ====================
@Composable
private fun TransactionItem(
    transaction: Transaction,
    isPersian: Boolean,
    formatter: NumberFormat,
    onClick: (Transaction) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick(transaction) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // آیکون دسته‌بندی
            Box(
                modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = getCategoryEmoji(transaction.category), fontSize = MaterialTheme.typography.headlineMedium.fontSize)
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "${formatter.format(transaction.amount)} تومان",
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun getCategoryEmoji(category: String): String = when (category) {
    "غذا" -> "🍔"
    "سوخت" -> "⛽"
    "حقوق" -> "💼"
    "خرید" -> "🛒"
    "اینترنت" -> "📞"
    else -> "📌"
}

// Dummy Data
private fun getDummyTransactions(): List<Transaction> = listOf(
    Transaction("1", "رستوران و غذا", -250000, "۱:۳۰ • امروز", "غذا"),
    Transaction("2", "سوخت", -800000, "۱۸:۴۵ • دیروز", "سوخت"),
    Transaction("3", "حقوق", 25000000, "۹:۰۰ • پیش ۲ روز", "حقوق"),
    Transaction("4", "خرید", -430000, "۱۶:۲۰ • پیش ۳ روز", "خرید"),
    Transaction("5", "اینترنت و تلفن", -120000, "۱۱:۱۰ • پیش ۳ روز", "اینترنت")
)

// ==================== Bottom Navigation ====================
@Composable
private fun BottomNavigationBar(isPersian: Boolean) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(if (isPersian) "مین" else "Home") },
            selected = true,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CompareArrows, contentDescription = null) },
            label = { Text(if (isPersian) "تراکنش‌ها" else "Transactions") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.BarChart, contentDescription = null) },
            label = { Text(if (isPersian) "آنالیز" else "Analytics") },
            selected = false,
            onClick = {}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(if (isPersian) "تنظیمات" else "Settings") },
            selected = false,
            onClick = {}
        )
    }
}