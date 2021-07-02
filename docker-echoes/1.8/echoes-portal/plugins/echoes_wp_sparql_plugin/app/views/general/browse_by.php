<?php
$siteUrl   = get_site_url();
$photoPath = get_bloginfo('template_url');
$date      = $_GET['date'];
$type      = $_GET['type'];
echo do_shortcode(
<<<EOT
<div class="category">
   <div class="title-browse">
      <span class="categories"><img src="$photoPath/img/Assets/ico-menu-categories.svg" /><h3><strong>Filter by</strong></h3></span>
EOT
);
if($type != "agent") {
echo do_shortcode(
<<<EOT
        <div class="category-list">
                <span><img src="$photoPath/img/Assets/ico-people-violet.svg" /><a href="$siteUrl/timespans/relations/?date=$date&page=1&type=agent">&nbsp;&nbsp;&nbsp;&nbsp;People</a></span>
       </div>
EOT
);
} else if($type != "cho") {
echo do_shortcode(
<<<EOT
       <div class="category-list">
                <span><img src="$photoPath/img/Assets/ico-digital-objets-violet.svg" /><a href="$siteUrl/timespans/relations/?date=$date&page=1&type=cho">&nbsp;&nbsp;&nbsp;&nbsp;Providers</a></span>
       </div>
EOT
);
}
echo do_shortcode(
<<<EOT
        </div>
</div>
EOT
);
