<?php 
        $url = $_SERVER['HTTP_REFERER'];
        if (strpos($url, '/ca/') !== false) {
                $current_locale = '/ca';
                $people_label = "Llistat complet de persones";
        } elseif (strpos($url, '/nl/') !== false) {
                $current_locale = '/nl';
                $people_label = "Volledige lijst met mensen";
	} elseif (strpos($url, '/fy/') !== false) {
                $current_locale = '/fy';
                $people_label = "Folsleine list mei minsken";
        } else {
                $current_locale = '';
                $people_label = "Full list People"; 
        }
?>
<?php foreach ($agents as $agent): 
	$year = explode('-', $agent->dateBirth->value)[0];?>
    <div class="people-list">
        <div class="people-details">
                <img style="vertical-align: middle; width: 16px" src="<?php echo WP_SITEURL ?>/wp-content/themes/echoes/img/Assets/ico-people-violet.svg"></img><a href="<?php echo WP_SITEURL . $current_locale; ?>/agents/details/?subject=<?php echo $agent->s->value ?>"><?php echo '  ' . $agent->name->value ?></a>
<?php if(!empty($agent->dateBirth->value)) : ?>
<div class='people-details-text'><img src="<?php echo WP_SITEURL ?>/wp-content/themes/echoes/img/Assets/birth.png" />&nbsp;&nbsp;<?php echo explode(':', $agent->placeBirth->value)[1] ?>, <?php echo date("d/m/Y", strtotime($agent->dateBirth->value)) ?></div>
<?php endif; ?>
<?php if(!empty($agent->dateDeath->value)) : ?>
<div class='people-details-text'><img src="<?php echo WP_SITEURL ?>/wp-content/themes/echoes/img/Assets/death.png" />&nbsp;&nbsp;<?php echo explode(':', $agent->placeDeath->value)[1] ?>,<?php echo date("d/m/Y", strtotime($agent->dateDeath->value)) ?></div>
<?php endif; ?>
        </div>
</div>
<?php endforeach; ?>
<a style="text-decoration: none;" href="<?php echo WP_SITEURL . $current_locale; ?>/timespans/?year=<?php echo $year ?>&type=agent"><?php echo $people_label ?></a>
