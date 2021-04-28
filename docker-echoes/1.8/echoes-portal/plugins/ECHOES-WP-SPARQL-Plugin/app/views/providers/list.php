<?php foreach ($chos["1"]['providedCHOs'] as $cho_id => $cho_info): ?>
    <div class="people-list">
        <div class="people-details">
                <span style="margin-left: 0px"><?php echo $this->icons->get_providedCHO_icon($cho_info['types']); ?> <a href="<?php echo WP_SITEURL; ?>/providers/details/?subject=<?php echo $cho_id; ?>"><?php echo implode(' ', $cho_info['titles']); ?></a></span>
        </div>
    </div>
<?php endforeach; ?>
<a style="text-decoration: none;" href="<?php echo WP_SITEURL; ?>/timespans/?year=<?php echo explode('-', $date)[0]; ?>&type=providedCHO">Full list Digital Objects</a>

