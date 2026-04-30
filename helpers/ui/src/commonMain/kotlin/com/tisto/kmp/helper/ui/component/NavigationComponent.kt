package com.tisto.kmp.helper.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tisto.kmp.helper.ui.icon.IcDot
import com.tisto.kmp.helper.ui.icon.MyIcon
import com.tisto.kmp.helper.ui.theme.Colors
import com.tisto.kmp.helper.ui.theme.Radius
import com.tisto.kmp.helper.ui.theme.Spacing
import com.tisto.kmp.helper.ui.theme.TextAppearance
import com.tisto.kmp.helper.utils.ext.def
import com.tisto.kmp.helper.utils.model.NavItem
import com.tisto.kmp.helper.utils.model.Navigation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

class UserProfile(
    val name: String = "",
    val userRole: String = ""
)

@Composable
fun NavigationViewSimple(
    isTablet: Boolean = false,
    modifier: Modifier = Modifier,
    listNav: List<Navigation> = emptyList(),
    currentRoute: String? = null,              // ✅ only this
    onNavigate: (NavItem) -> Unit = {},        // ✅ only this
    initialScrollPosition: Int = 0,
    onScrollChanged: (Int) -> Unit = {},
    userProfile: UserProfile = UserProfile(),
    logo: DrawableResource,
    onLogout: () -> Unit = {},
    onClose: (() -> Unit)? = null,
) {
    var showPopup by remember { mutableStateOf(false) }

    // selection auto detect
    val selection = remember(listNav, currentRoute) { findSelection(listNav, currentRoute) }
    val selectedMain = selection.main
    val selectedSub = selection.sub

    // expanded auto + toggle (store by MAIN ROUTE for simplicity)
    var expandedMainRoutes by rememberSaveable { mutableStateOf(emptyList<String>()) }

    // Auto-open parent when current route is a sub item
    LaunchedEffect(selectedMain?.route, selectedSub?.route) {
        if (selectedSub != null && selectedMain?.route != null) {
            val route = selectedMain.route
            if (!expandedMainRoutes.contains(route)) expandedMainRoutes = expandedMainRoutes + route
        }
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(initialScrollPosition) {
        scrollState.scrollTo(initialScrollPosition)
    }

    LaunchedEffect(scrollState.value) {
        onScrollChanged(scrollState.value)
    }

    Column(modifier = modifier.fillMaxHeight()) {

        // ===== Header =====
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(end = Spacing.small)
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(23.dp)
                    .align(Alignment.CenterStart)
                    .padding(start = Spacing.box)
            )

            if (!isTablet) onClose?.let {
                IconButton(
                    onClick = it,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
//                        imageVector = Icons.Default.Search,
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(23.dp)
                    )
                }
            }
        }

        // ===== Menu =====
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = Spacing.box)
                .padding(top = Spacing.small)
        ) {
            listNav.forEach { nav ->
                Text(
                    text = nav.name,
                    style = TextAppearance.title2Bold(),
                    color = Colors.Dark
                )
                Spacer(modifier = Modifier.height(Spacing.small))

                nav.navItem.forEach { main ->
                    val isSelectedMain = (main.route == selectedMain?.route)
                    val isExpanded = expandedMainRoutes.contains(main.route)

                    // MAIN ROW
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(Radius.normal))
                            .background(if (isSelectedMain) Colors.ColorPrimary50 else Colors.White)
                            .clickable {
                                if (main.subNavItems.isNotEmpty()) {
                                    expandedMainRoutes = expandedMainRoutes.toggle(main.route)
                                } else {
                                    onNavigate(main)
                                }
                            }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.width(Spacing.box))

                            Icon(
                                imageVector = main.icon ?: Icons.Default.Menu,
                                contentDescription = "Icon",
                                tint = if (isSelectedMain) Colors.ColorPrimary else Colors.Gray3,
                                modifier = Modifier.size(25.dp)
                            )

                            Text(
                                text = main.name,
                                style = TextAppearance.body1(),
                                color = if (isSelectedMain) Colors.ColorPrimary else Colors.Gray1,
                                modifier = Modifier
                                    .padding(Spacing.box)
                                    .weight(1f)
                            )

                            if (main.subNavItems.isNotEmpty()) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                                    contentDescription = "Expand",
                                    tint = Colors.Gray2,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(Spacing.box))
                            }
                        }
                    }

                    // SUB ITEMS
                    if (isExpanded && main.subNavItems.isNotEmpty()) {
                        Spacer(Modifier.height(Spacing.tiny))

                        main.subNavItems.forEach { sub ->
                            val isSelectedSub = (sub.route == selectedSub?.route)

                            Row {
                                Spacer(Modifier.width(Spacing.box))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(Radius.normal))
                                        .background(if (isSelectedSub) Colors.ColorPrimary50 else Colors.White)
                                        .clickable { onNavigate(sub) }
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(Modifier.width(Spacing.box))

                                        Icon(
                                            imageVector = IcDot,
                                            contentDescription = "Dot",
                                            tint = if (isSelectedSub) Colors.ColorPrimary else Colors.Gray3,
                                            modifier = Modifier.size(15.dp)
                                        )

                                        Text(
                                            text = sub.name,
                                            style = TextAppearance.body1(),
                                            color = if (isSelectedSub) Colors.ColorPrimary else Colors.Gray2,
                                            modifier = Modifier.padding(Spacing.box)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(Spacing.tiny))
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.small))
            }
        }

        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 0.4.dp)

        // ===== Footer (profile) =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPopup = !showPopup }
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = Spacing.small, horizontal = Spacing.box)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Colors.ColorPrimary,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "AD",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box {
                    PopupMenu(
                        expanded = showPopup,
                        onDismiss = { showPopup = false },
                        items = listOf("Logout"),
                        onSelected = { if (it == "Logout") onLogout() }
                    )
                }

                Spacer(Modifier.width(Spacing.box))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.name.def("App Admin"),
                        style = TextAppearance.body2Bold(),
                        color = Colors.Dark
                    )
                    Text(
                        text = userProfile.userRole.def("Staff"),
                        style = TextAppearance.body3(),
                        color = Colors.Gray2
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Menu",
                    tint = Colors.Gray2,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(Modifier.width(Spacing.box))
            }
        }
    }
}

private data class NavSelection(
    val main: NavItem? = null,
    val sub: NavItem? = null
)

/**
 * Auto-detect selected main/sub from currentRoute.
 * - If currentRoute matches a main item -> main selected
 * - If currentRoute matches a sub item -> parent main selected + sub selected
 */
private fun findSelection(
    listNav: List<Navigation>,
    currentRoute: String?
): NavSelection {
    if (currentRoute.isNullOrBlank()) return NavSelection()

    listNav.forEach { group ->
        group.navItem.forEach { main ->
            if (main.route == currentRoute) return NavSelection(main = main)
            main.subNavItems.forEach { sub ->
                if (sub.route == currentRoute) return NavSelection(main = main, sub = sub)
            }
        }
    }
    return NavSelection()
}

private fun List<String>.toggle(value: String): List<String> =
    if (contains(value)) filterNot { it == value } else this + value


fun List<Navigation>.findNavItemByRoute(route: String?): NavItem? {
    val listNav: List<Navigation> = this
    if (route.isNullOrBlank()) return null
    listNav.forEach { group ->
        group.navItem.forEach { main ->
            if (main.route == route) return main
            main.subNavItems.forEach { sub ->
                if (sub.route == route) return sub
            }
        }
    }
    return null
}


fun findNavItemByRoutes(listNav: List<Navigation>, route: String?): NavItem? {
    if (route.isNullOrBlank()) return null
    listNav.forEach { group ->
        group.navItem.forEach { main ->
            if (main.route == route) return main
            main.subNavItems.forEach { sub ->
                if (sub.route == route) return sub
            }
        }
    }
    return null
}
