package com.minhtdh.common.widget;

import android.content.Context;
import android.util.SparseIntArray;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import java.util.Comparator;
import java.util.List;

/**
 * use for ArrayAdapter with Alphabet Indexer
 * use code from AlphabetIndexer 
 * @author MinhTDH
 * Jan 23, 2014
 */
public abstract class AbstractAlphabeAdapter<T> extends ArrayAdapter<T> implements SectionIndexer {

    private static final String mAlphabet;
    private static final String[] indexer;
    private static final int idxLength;
    private SparseIntArray mAlphaMap;
    protected final java.text.Collator mCollator;
    
    static {
        mAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ#";
        idxLength = mAlphabet.length();
        indexer = new String[idxLength];
        for (int i = 0; i < idxLength; i++) {
            indexer[i] = Character.toString(mAlphabet.charAt(i));
        }
    }
    
    {
        mAlphaMap = new SparseIntArray(idxLength);
        mCollator = java.text.Collator.getInstance();
        mCollator.setStrength(java.text.Collator.PRIMARY);
    }

    public AbstractAlphabeAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AbstractAlphabeAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public AbstractAlphabeAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public AbstractAlphabeAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public AbstractAlphabeAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);
    }

    public AbstractAlphabeAdapter(Context context, int textViewResourceId, T[] objects) {
        super(context, textViewResourceId, objects);
    }
    
    @Override
    public int getPositionForSection(int sectionIndex) {
        final SparseIntArray alphaMap = mAlphaMap;

        // Check bounds
        if (sectionIndex <= 0) {
            return 0;
        }
        if (sectionIndex >= idxLength) {
            sectionIndex = idxLength - 1;
        }

        int count = getCount();
        int start = 0;
        int end = count;
        int pos;

        char letter = mAlphabet.charAt(sectionIndex);
        String targetLetter = Character.toString(letter);
        int key = letter;
        // Check map
        if (Integer.MIN_VALUE != (pos = alphaMap.get(key, Integer.MIN_VALUE))) {
            // Is it approximate? Using negative value to indicate that it's
            // an approximation and positive value when it is the accurate
            // position.
            if (pos < 0) {
                pos = -pos;
                end = pos;
            } else {
                // Not approximate, this is the confirmed start of section, return it
                return pos;
            }
        }

        // Do we have the position of the previous section?
        if (sectionIndex > 0) {
            int prevLetter = mAlphabet.charAt(sectionIndex - 1);
            int prevLetterPos = alphaMap.get(prevLetter, Integer.MIN_VALUE);
            if (prevLetterPos != Integer.MIN_VALUE) {
                start = Math.abs(prevLetterPos);
            }
        }

        // Now that we have a possibly optimized start and end, let's binary search

        pos = (end + start) / 2;

        while (pos < end) {
            String curName = getTitleForPosition(pos);
            if (curName == null) {
                if (pos == 0) {
                    break;
                } else {
                    pos--;
                    continue;
                }
            }
            int diff = compare(curName, targetLetter);
            if (diff != 0) {
                // TODO: Commenting out approximation code because it doesn't work for certain
                // lists with custom comparators
                // Enter approximation in hash if a better solution doesn't exist
                // String startingLetter = Character.toString(getFirstLetter(curName));
                // int startingLetterKey = startingLetter.charAt(0);
                // int curPos = alphaMap.get(startingLetterKey, Integer.MIN_VALUE);
                // if (curPos == Integer.MIN_VALUE || Math.abs(curPos) > pos) {
                // Negative pos indicates that it is an approximation
                // alphaMap.put(startingLetterKey, -pos);
                // }
                // if (mCollator.compare(startingLetter, targetLetter) < 0) {
                if (diff < 0) {
                    start = pos + 1;
                    if (start >= count) {
                        pos = count;
                        break;
                    }
                } else {
                    end = pos;
                }
            } else {
                // They're the same, but that doesn't mean it's the start
                if (start == pos) {
                    // This is it
                    break;
                } else {
                    // Need to go further lower to find the starting row
                    end = pos;
                }
            }
            pos = (start + end) / 2;
        }
        alphaMap.put(key, pos);
        return pos;
    }
    
    @Override
    public Object[] getSections() {
        return indexer;
    }
    
    @Override
    public int getSectionForPosition(int position) {
        String curName = getTitleForPosition(position);
        // Linear search, as there are only a few items in the section index
        // Could speed this up later if it actually gets used.
        for (int i = 0; i < idxLength; i++) {
            char letter = mAlphabet.charAt(i);
            String targetLetter = Character.toString(letter);
            if (compare(curName, targetLetter) == 0) {
                return i;
            }
        }
        return 0; // Don't recognize the letter - falls under zero'th section
    }
    
    public abstract String getTitleForPosition(int position);
    
    protected int compare(String word, String letter) {
        final String firstLetter;
        if (word == null || word.length() == 0) {
            firstLetter = " ";
        } else {
            firstLetter = word.substring(0, 1);
        }

        return mCollator.compare(firstLetter, letter);
    }
    
    @Override
    public void notifyDataSetChanged() {
        setNotifyOnChange(false);
        sort(getComparator());
        setNotifyOnChange(false);
        super.notifyDataSetChanged();
        mAlphaMap.clear();
    }
    
    protected abstract Comparator<? super T> getComparator();
    
    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        mAlphaMap.clear();
    }

}
